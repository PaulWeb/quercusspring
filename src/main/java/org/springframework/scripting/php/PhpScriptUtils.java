/*
 * Copyright (c) 2013 World Page Company -- all rights reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.scripting.php;

import com.caucho.quercus.QuercusContext;
import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.StringValue;
import com.caucho.quercus.env.Value;
import com.caucho.quercus.page.QuercusPage;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.log4j.Logger;
import org.springframework.core.NestedRuntimeException;
import org.springframework.util.ReflectionUtils;
import com.caucho.quercus.QuercusRuntimeException;
import com.caucho.quercus.env.ObjectExtValue;
import com.caucho.quercus.env.ObjectValue;
import com.caucho.quercus.env.QuercusClass;
import com.caucho.quercus.program.QuercusProgram;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is designed for creating java proxy instance according specification of interface
 * from php files.
 * @author Paul Shishakov <pauladnweb@gmail.com>
 * @author Sergej Varjuhin <cepreu.mail@gmail.com>
 * @see http://quercus.caucho.com/quercus-3.1/doc/quercus.xtp
 */
public abstract class PhpScriptUtils {

    private static final Logger log = Logger.getLogger(PhpScriptUtils.class);

    /**
     * create proxy object 
     * @param env - quercus environment
     * @param scriptSource - script or path
     * @param interfaces 
     * @param classLoader
     * @param superclass -  class which implements interfaces
     * @return proxy object
     * @throws IOException 
     */

    public static Object createPHPObject(QuercusContext context, String scriptSource, Class[] interfaces, ClassLoader classLoader) throws IOException {

        QAdapter adapter = new QAdapter();
        Object result = null;
        Env env = null;
        try {
            env = new Env(context);
            env.setRuntimeEncoding("UTF-8");
            env.start();
            env.setTimeLimit(0); // practically disables time limit
            env.resetTimeout();
            env.setIni("display_errors", "stderr");
            adapter.compile(env, scriptSource, interfaces);
            result = Proxy.newProxyInstance(classLoader, interfaces, new PHPObjectInvocationHandler(adapter));
        } catch (Exception ex) {
            if (env != null) {
                env.close();
            }
            throw new PHPEnvException(ex);
        } finally {
        }
        return result;
    }

    private static class PHPObjectInvocationHandler implements InvocationHandler {

        private static final Map<Class<?>, IConvertor> _wrapper = wrapper();

        private static Map<Class<?>, IConvertor> wrapper() {
            Map<Class<?>, IConvertor> mapClass = new HashMap<Class<?>, IConvertor>(29);
            mapClass.put(Integer.class, new IConvertor<Integer>() {

                public Integer convert(Value value, Env env) {
                    return value.toInt();
                }
            });
            mapClass.put(Object.class, new IConvertor<Object>() {

                public Object convert(Value value, Env env) {
                    return value.toJavaObject();
                }
            });
            mapClass.put(List.class, new IConvertor<List>() {

                public List convert(Value value, Env env) {
                    return value.toJavaList(env, Object.class);
                }
            });
            mapClass.put(Map.class, new IConvertor<Map>() {

                public Map convert(Value value, Env env) {
                    return value.toJavaMap(env, Object.class);
                }
            });
            mapClass.put(int.class, mapClass.get(Integer.class));
            mapClass.put(long.class, mapClass.get(Object.class));
            mapClass.put(Long.class, mapClass.get(Object.class));
            mapClass.put(HashMap.class, mapClass.get(Map.class));
            return Collections.unmodifiableMap(mapClass);
        }

        private interface IConvertor<T> {
            T convert(Value value, Env env);
        }
        QAdapter adptr;
        private Map<String, Value> classes = new HashMap<String, Value>();

        public PHPObjectInvocationHandler(QAdapter adapter) {
            this.adptr = adapter;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (ReflectionUtils.isHashCodeMethod(method)) {
                return this.adptr.hashCode();
            } else if (ReflectionUtils.isToStringMethod(method)) {
                return this.adptr.toString();
            } else if (ReflectionUtils.isEqualsMethod(method)) {
                return this.adptr.equals(args[0]);
            }
            try {
                Value phpresult = this.adptr.call(method, convertToValues(args));
                return convertFromPhp(phpresult, method.getReturnType());
            } catch (QuercusRuntimeException ex) {
                throw new PHPExecutionException(ex);
            }

        }

        private Object convertFromPhp(Value val, Class<?> cls) {
            Object result = null;
            if(val!=null){
                if (cls != null && _wrapper.containsKey(cls)) {
                    result = _wrapper.get(cls).convert(val, adptr.getEnv());
                } else {
                    result = val.isArray() ? val.toJavaList(adptr.getEnv(), Object.class) : val.toJavaObject();
                }
            }
            return result;
        }

        private Value[] convertToValues(Object[] args) {
            if (args == null || args.length == 0) {
                return new Value[0];
            }
            Value[] phpArgs = new Value[args.length];
            for (int i = 0; i < args.length; ++i) {
                if (args[i] != null && args[i].getClass() == Character.class) // due to prevent bug in Quercus: Env.wrapJava(Character c) fails.
                    args[i] = ((Character)args[i]).toString();
                if (args[i] instanceof char[])
                    args[i] = new String((char[]) args[i]);
                if (args[i] instanceof byte[])
                    args[i] = new String((byte[]) args[i]);
                phpArgs[i] = adptr.getEnv().wrapJava(args[i]);
            }
            return phpArgs;
        }
    }

    public static class PHPEnvException extends NestedRuntimeException {

        public PHPEnvException(Exception ex) {
            super(ex.getMessage(), ex);
        }
    }

    private static class QAdapter {

        private QuercusPage page;
        private QuercusProgram program;
        private Env env;
        private Value value;
        private Class superClass;
        private boolean isCompiled = false;

        public void setSuperClass(Class cls) {
            if (cls != null)
                this.superClass = cls;
        }

        public final Env getEnv() {
            return this.env;
        }

        public void compile(Env env, String path, Class[] interfaces) {
            compile(env, path);
            setSuperClass(interfaces[0]);
        }

        private void compile(Env env, String path) {
            try {
                this.env = env;
                this.program = env.getQuercus().parseCode(path.replace("<?php", "").replace("?>", ""));
                this.program.execute(env);                 
                this.isCompiled = true;
            } catch (Exception ex) {
                throw new PHPExecutionException(ex);
            }
        }

        public Value call(Method method, Value[] vls) {
            return call(method.getName(), vls);
        }

        public Value call(String method, Value[] vls) {
            if (!this.isCompiled) {
                return null;
            }
            Value val=null;
            if (this.page != null && page.isModified()) {
                try {
                    this.page = this.env.getQuercus().parse(this.page.getSelfPath(this.env));
                    this.page.execute(this.env);
                } catch (Exception ex) {
                    throw new PHPEnvException(ex);
                }
                this.value = null;
            }
            try{
                if (this.value == null) {
                    QuercusClass qc = this.env.findClass(this.superClass.getSimpleName());
                    if (qc == null) {
                        throw new NullPointerException("Can't find PHP class with name:" + this.superClass.getSimpleName());
                    }
                    ObjectValue ov = new ObjectExtValue(qc);
                    qc.initObject(this.env, ov);
                    this.value = qc.callNew(this.env, ov);
                }
                val = this.value.callMethod(this.env, (StringValue) StringValue.create(method), vls);
            }catch(NullPointerException  ex){
                throw new PHPExecutionException((Exception)ex);
            }catch(Throwable ex){
                if(log.isDebugEnabled()){
                    log.error(ex.getMessage(), ex);
                }
                throw new PHPExecutionException((Exception)ex);
            }
            return val;
        }

        @Override
        public String toString() {
            StringBuilder _sb=new StringBuilder();
            _sb.append("QAdapter[mode=")
               .append(",compiled=")
               .append(this.isCompiled)
               .append("]");
            return _sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return this.hashCode() == obj.hashCode();
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 13 * hash + (this.page != null ? this.page.hashCode() : 0);
            hash = 13 * hash + (this.program != null ? this.program.hashCode() : 0);
            hash = 13 * hash + (this.env != null ? this.env.hashCode() : 0);
            hash = 13 * hash + (this.superClass != null ? this.superClass.hashCode() : 0);
            return hash;
        }
    }

    public static class PHPExecutionException extends NestedRuntimeException {

        /**
         * Create a new <code>PHPExecutionException</code>,
         * wrapping the given PHP <code>RaiseException</code>.
         * @param ex the cause (must not be <code>null</code>)
         */
        public PHPExecutionException(Exception ex) {
            super("Runtime Exception", ex);
        }

        public PHPExecutionException(QuercusRuntimeException ex) {
            super(buildMessage(ex), ex);
        }

        private static String buildMessage(QuercusRuntimeException ex) {
            String _message = "Runtime Exception: " + ex.getMessage();
            if (log.isDebugEnabled()) {
                log.error(_message, ex);
            }
            return _message;
        }
    }
}
