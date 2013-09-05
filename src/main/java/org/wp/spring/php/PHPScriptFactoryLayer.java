/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wp.spring.php;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author user
 */
public class PHPScriptFactoryLayer {
    
    private static PHPScriptFactory scriptFactory;
    
    @Autowired
    public void setFactory(PHPScriptFactory factory){
        scriptFactory = factory;
    }
    
    public static Object create(String scriptSourceLocator, String scriptInterfaces) throws ClassNotFoundException {
        String[] list = scriptInterfaces.split(",");
        Class<?>[] classes = new Class<?>[list.length];
        for (int i = 0; i < list.length; i++)
            classes[i] = Class.forName(list[i].trim());
        return scriptFactory.create(scriptSourceLocator, classes, null);
    }
    
}
