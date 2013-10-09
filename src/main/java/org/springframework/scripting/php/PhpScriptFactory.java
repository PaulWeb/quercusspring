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
import java.io.IOException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.scripting.ScriptCompilationException;
import org.springframework.scripting.ScriptFactory;
import org.springframework.scripting.ScriptSource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 *
 * @author Sergej Varjuhin <cepreu.mail@gmail.com>
 * @author Paul Shishakov <paulandweb@gmail.com>
 * 
 */
public class PhpScriptFactory implements ScriptFactory, BeanClassLoaderAware {

    private final String scriptSourceLocator;
    private final Class[] scriptInterfaces;
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private QuercusContext phpContext;
    
    public PhpScriptFactory(String scriptSourceLocator, Class[] scriptInterfaces) {
        Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
        Assert.notEmpty(scriptInterfaces, "'scriptInterfaces' must not be empty");
        this.scriptSourceLocator = scriptSourceLocator;
        this.scriptInterfaces = scriptInterfaces;
        this.phpContext = new QuercusContext();
        this.phpContext.init();
        this.phpContext.start();
    }
    
    public String getScriptSourceLocator() {
        return this.scriptSourceLocator;
    }

    public Class[] getScriptInterfaces() {
        return this.scriptInterfaces;
    }

    public boolean requiresConfigInterface() {
        return true;
    }

    public Object getScriptedObject(ScriptSource ss, Class[] types) throws IOException, ScriptCompilationException {
        return PhpScriptUtils.createPHPObject(this.phpContext, ss.getScriptAsString(), types, this.beanClassLoader);
    }

    public Class getScriptedObjectType(ScriptSource ss) throws IOException, ScriptCompilationException {
        return null;
    }

    public boolean requiresScriptedObjectRefresh(ScriptSource ss) {
        return ss.isModified();
    }

    public void setBeanClassLoader(ClassLoader cl) {
        this.beanClassLoader = cl;
    }
    
}
