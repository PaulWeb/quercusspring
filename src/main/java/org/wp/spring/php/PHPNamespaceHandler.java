/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wp.spring.php;

import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 *
 * @author user
 */
public class PHPNamespaceHandler extends NamespaceHandlerSupport implements NamespaceHandler {
    
    public void init() {
        registerBeanDefinitionParser("php", new PHPBeanDefinitionParser());
    }
    
}
