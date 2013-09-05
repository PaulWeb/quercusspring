/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wp.spring.php;

import java.util.List;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.util.xml.DomUtils;
import org.springframework.beans.factory.xml.ParserContext;

/**
 *
 * @author user
 */
public class PHPBeanDefinitionParser extends AbstractSingleBeanDefinitionParser{

    @Override
    protected String getBeanClassName(Element element) {
        return "org.wp.spring.php.PHPScriptFactoryLayer";
    }
    
    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        bean.setFactoryMethod("create");
        String scriptSource;
        List elements = DomUtils.getChildElementsByTagName(element, "inline-script");
        if (!elements.isEmpty())
            scriptSource = "<?php" + DomUtils.getTextValue((Element) elements.get(0)) + "?>";
        else
            scriptSource = element.getAttribute("script-source");
        bean.addConstructorArgValue(scriptSource);
        bean.addConstructorArgValue(element.getAttribute("script-interfaces"));
        parserContext.getDelegate().parsePropertyElements(element, bean.getRawBeanDefinition());
    }
    
}
