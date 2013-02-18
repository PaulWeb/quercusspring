/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wp.spring.php;

import com.caucho.quercus.QuercusContext;
import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.StringValue;
import com.caucho.quercus.env.Value;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.vfs.FilePath;
import com.caucho.vfs.Path;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.log4j.Logger;
import org.springframework.core.NestedRuntimeException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import com.caucho.quercus.QuercusRuntimeException;
import com.caucho.quercus.env.ObjectExtValue;
import com.caucho.quercus.env.ObjectValue;
import com.caucho.quercus.env.QuercusClass;
import com.caucho.quercus.function.AbstractFunction;
import com.caucho.quercus.program.Function;
import com.caucho.quercus.program.QuercusProgram;
import com.caucho.vfs.ReadStream;
import com.caucho.vfs.StreamImpl;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

/**
 * This class is designed for creating java proxy instance according specification of interface
 * from php files.
 * @author Paul Shishakov
 * @e-mail: pauladnweb@gmail.com
 * @see http://quercus.caucho.com/quercus-3.1/doc/quercus.xtp
 */
public abstract class PHPScriptUtils {

    private static final Logger log = Logger.getLogger(PHPScriptUtils.class);

    public enum MODE {

        SUPER, PAGE, PROGRAMM
    }

    /**
     * Determine whether the str is a path or script
     * @param str - script source
     * @return true if it is script or false if it is file
     */
    public static boolean isItScript(String str) {
        return (str != null && !str.isEmpty() && str.indexOf("<?php") >= 0);
    }

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
    public static Object createPHPObject(Env env, String scriptSource, Class[] interfaces, ClassLoader classLoader, Class superclass) throws IOException {
        if (env == null) {
            throw new NullPointerException();
        }
        String script = scriptSource.trim().replace("\n", "");
        QAdapter _adapter = new QAdapter();
        Object result = null;
        try {
            _adapter.compile(env, script, superclass);
            result = Proxy.newProxyInstance(classLoader, interfaces, new PHPObjectInvocationHandler(_adapter));

        } catch (Exception ex) {

            throw new PHPEnvException(ex);
        }
        return result;
    }

    public static Object createPHPObject(QuercusContext context, String scriptSource, Class[] interfaces, ClassLoader classLoader, Class superclass) throws IOException {

        QAdapter _adapter = new QAdapter();
        Object result = null;
        Env env = null;
        try {
            env = new Env(context);          
            env.start();
            _adapter.compile(env, scriptSource, superclass);
            result = Proxy.newProxyInstance(classLoader, interfaces, new PHPObjectInvocationHandler(_adapter));
        } catch (Exception ex) {
            if (env != null) {
                env.close();
            }
            throw new PHPEnvException(ex);
        } finally {
        }
        return result;
    }

    /**
     * lookup location in $user.dir
     * @param location - file or path examples: test.php, dir/test.php  
     * @return Path for quercus
     */
    private static Path getPath(String location) {
        FilePath pwd = new FilePath(System.getProperty("user.dir"));
        return pwd.lookup(location);
    }

    private static class PHPObjectInvocationHandler implements InvocationHandler {

        private static final Map<Class<?>, IConvertor> _wrapper = wrapper();

        private static final Map<Class<?>, IConvertor> wrapper() {
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
        QAdapter _adptr;
        private Map<String, Value> _classes = new HashMap<String, Value>();

        public PHPObjectInvocationHandler(QAdapter adapter) {
            this._adptr = adapter;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (ReflectionUtils.isHashCodeMethod(method)) {
                return _adptr.hashCode();
            } else if (ReflectionUtils.isToStringMethod(method)) {
                return _adptr.toString();
            } else if (ReflectionUtils.isEqualsMethod(method)) {
                return _adptr.equals(args[0]);
            }
            try {

                Value phpresult = _adptr.call(method, convertToValues(args));
                return convertFromPhp(phpresult, method.getReturnType());
            } catch (QuercusRuntimeException ex) {
                throw new PHPExecutionException(ex);
            }

        }

        private Object convertFromPhp(Value val, Class<?> cls) {
            Object result = null;
            if(val!=null){
                if (cls != null && _wrapper.containsKey(cls)) {
                    result = _wrapper.get(cls).convert(val, _adptr.getEnv());
                } else {
                    result = val.isArray() ? val.toJavaList(_adptr.getEnv(), Object.class) : val.toJavaObject();
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
                phpArgs[i] = _adptr.getEnv().wrapJava(args[i]);
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

        private QuercusPage _page;
        private QuercusProgram _program;
        private Env _env;
        private Value _value;
        private Class _superClass;
        private MODE _mode;
        private boolean _isCompiled = false;

        public void setSuperClass(Class cls) {
            if (cls != null) {
                this._superClass = cls;
                this._mode = MODE.SUPER;
            }
        }

        public final Env getEnv() {
            return this._env;
        }

        public void compile(Env env, String path, Class superClass) {
            compile(env, path);
            setSuperClass(superClass);
        }

        public void compile(Env env, String path) {
            try {
                this._env = env;
                if (!isItScript(path)) {
                    _page = env.getQuercus().parse(getPath(path));
                    _page.execute(env);
                    _mode = MODE.PAGE;

                } else {
                    _program = env.getQuercus().parseCode(path.replace("<?php", "").replace("?>", ""));
                    _program.execute(env);                 
                    _mode = MODE.PROGRAMM;
                }
                this._isCompiled = true;
            } catch (Exception ex) {
                throw new PHPExecutionException(ex);
            }
        }

        public Value call(Method method, Value[] vls) {
            return call(method.getName(), vls);
        }

        public Value call(String method, Value[] vls) {
            if (!this._isCompiled) {
                return null;
            }
            Value _val=null;
            if (_page != null && _page.isModified()) {
                try {
                    _page = _env.getQuercus().parse(_page.getSelfPath(_env));
                    _page.execute(_env);
                } catch (Exception ex) {
                    throw new PHPEnvException(ex);
                }
                _value = null;
            }
            try{
                switch (_mode) {
                    case PAGE:
                        _val= _page.findFunction(method).call(_env, vls);
                        break;
                    case PROGRAMM:
                        _val= _env.findFunction(method).call(_env, vls);
                        break;
                    case SUPER:
                        if (_value == null) {
                            QuercusClass qc = _env.findClass(_superClass.getSimpleName());
                            ObjectValue ov = new ObjectExtValue(qc);
                            qc.initObject(_env, ov);
                            _value = qc.callNew(_env, ov);
                        }
                        _val= _value.callMethod(_env, (StringValue) StringValue.create(method), vls);
                        break;

                }
            }catch(NullPointerException  ex){

                throw new PHPExecutionException((Exception)ex);
            }catch(Throwable ex){
                if(log.isDebugEnabled()){
                    log.error(ex.getMessage(), ex);
                }
                throw new PHPExecutionException((Exception)ex);
            }
            return _val;
        }

        @Override
        public String toString() {
            StringBuilder _sb=new StringBuilder();
            _sb.append("QAdapter[mode=")
               .append(this._mode)
               .append(",compiled=")
               .append(this._isCompiled)
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
            hash = 13 * hash + (this._page != null ? this._page.hashCode() : 0);
            hash = 13 * hash + (this._program != null ? this._program.hashCode() : 0);
            hash = 13 * hash + (this._env != null ? this._env.hashCode() : 0);
            hash = 13 * hash + (this._superClass != null ? this._superClass.hashCode() : 0);
            hash = 13 * hash + (this._mode != null ? this._mode.hashCode() : 0);
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
