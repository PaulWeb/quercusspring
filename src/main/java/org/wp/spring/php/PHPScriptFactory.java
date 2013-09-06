/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wp.spring.php;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.scripting.ScriptCompilationException;
import org.springframework.scripting.ScriptFactory;
import org.springframework.scripting.ScriptSource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 *{@link org.springframework.scripting.ScriptFactory} implementation
 * for a PHP(Quercus) script.
 * 
 * @author Paul Shishakov
 * @e-mail:paulandweb@gmail.om
 */
public class PHPScriptFactory implements ScriptFactory, BeanClassLoaderAware {

    private Logger _log = Logger.getLogger(PHPScriptFactory.class);
    private String scriptSourceLocator;
    private Class[] scriptInterfaces;
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private PHPContext _phpContext;
    private PHPEnvironment _phpEnv;

    public void setQuercusContext(PHPContext context) {
        this._phpContext = context;
    }

    public PHPContext getQuercusContext() {
        return _phpContext;
    }

    public void setEnvironment(PHPEnvironment env) {
        this._phpEnv = env;
        this._phpContext = env.getContext();
    }

    public PHPEnvironment getEnvironment() {
        return this._phpEnv;
    }

    public Object create(String scriptSourceLocator, Class[] scriptInterfaces) {
        return create(scriptSourceLocator, scriptInterfaces, null);
    }
    
    public Object create(String scriptSourceLocator, Class[] scriptInterfaces, Class superClass) {
        Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
        Assert.notEmpty(scriptInterfaces, "'scriptInterfaces' must not be empty");
        this.scriptSourceLocator = scriptSourceLocator;
        this.scriptInterfaces = scriptInterfaces;
        Object result = null;

        try {
            if (this._phpEnv == null) {
                result = PHPScriptUtils.createPHPObject(_phpContext.getContext(), StringEscapeUtils.unescapeHtml(scriptSourceLocator), scriptInterfaces, beanClassLoader, superClass);
            } else {
                result = PHPScriptUtils.createPHPObject(_phpEnv.getEnvironment(), StringEscapeUtils.unescapeHtml(scriptSourceLocator), scriptInterfaces, beanClassLoader, superClass);
            }
        } catch (Exception ex) {
            if (_log.isDebugEnabled()) {
                _log.error("fatal error", ex);
            }
        }
        return result;
    }

    @Override
    public String getScriptSourceLocator() {
        return this.scriptSourceLocator;
    }

    @Override
    public Class[] getScriptInterfaces() {
        return this.getScriptInterfaces();
    }

    @Override
    public boolean requiresConfigInterface() {
        return true;
    }

    @Override
    public Object getScriptedObject(ScriptSource ss, Class[] types) throws IOException, ScriptCompilationException {
        Object result = null;
        try {
            result = PHPScriptUtils.createPHPObject(_phpContext.getContext(), scriptSourceLocator, scriptInterfaces, beanClassLoader,null);
        } catch (Exception ex) {
            throw new ScriptCompilationException(ss, ex);
        }
        return result;
    }

    @Override
    public Class getScriptedObjectType(ScriptSource ss) throws IOException, ScriptCompilationException {
        return null;
    }

    @Override
    public boolean requiresScriptedObjectRefresh(ScriptSource ss) {
        return ss.isModified();
    }

    @Override
    public void setBeanClassLoader(ClassLoader cl) {
        this.beanClassLoader = cl;
    }
}
