/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import org.wp.spring.php.PHPScriptUtils;

/**
 *
 * @author user
 */
public class PhpScriptFactory implements ScriptFactory, BeanClassLoaderAware {

    private final String scriptSourceLocator;
    private final Class[] scriptInterfaces;
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private QuercusContext quercusContext;
    
    public PhpScriptFactory(String scriptSourceLocator, Class[] scriptInterfaces) {
        Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
        Assert.notEmpty(scriptInterfaces, "'scriptInterfaces' must not be empty");
        this.scriptSourceLocator = scriptSourceLocator;
        this.scriptInterfaces = scriptInterfaces;
        quercusContext = new QuercusContext();
        quercusContext.init();
        quercusContext.start();
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
        return PHPScriptUtils.createPHPObject(quercusContext, ss.getScriptAsString(), types, beanClassLoader, null);
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
