/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.scripting.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 *
 * @author user
 */
public class LangNamespaceHandlerWithPhpSupport extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerScriptBeanDefinitionParser("php", "org.springframework.scripting.php.PhpScriptFactory");
		registerBeanDefinitionParser("defaults", new ScriptingDefaultsParser());
	}

	private void registerScriptBeanDefinitionParser(String key, String scriptFactoryClassName) {
		registerBeanDefinitionParser(key, new ScriptBeanDefinitionParser(scriptFactoryClassName));
	}

}