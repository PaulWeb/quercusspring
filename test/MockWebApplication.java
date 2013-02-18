/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Paul Shishakov
 * @e-mail paulandweb@gmail.com
 * @see http://code.google.com/p/ted-young/source/browse/trunk/blog.spring-mvc-integration-testing/src/main/java/me/tedyoung/blog/spring_mvc_integration_testing/MockWebApplication.java
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.context.WebApplicationContext;

/**
 * Configures a mock {@link WebApplicationContext}.  Each test class (or parent class) using
 * {@link MockWebApplicationContextLoader} must be annotated with this.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MockWebApplication {
        /**
         * The location of the webapp directory relative to your project.
         * For maven users, this is generally src/main/webapp (default).
         */
        String webapp() default "src/main/webapp";
       
        /**
         * The servlet name as defined in the web.xml.
         */
        String name();
}


