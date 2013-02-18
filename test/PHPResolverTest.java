/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.context.ApplicationContext;
import javax.inject.Inject;
import org.springframework.web.servlet.HandlerExecutionChain;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.mock.web.MockHttpServletResponse;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.springframework.test.web.ModelAndViewAssert.*;
/**
 *
 * @author Paul
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-dispetcher.xml")
public class PHPResolverTest {
    Logger _log=Logger.getLogger(PHPResolverTest.class);
   @Inject
    private ApplicationContext applicationContext;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private HandlerAdapter handlerAdapter;
    
    public PHPResolverTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();

        this.handlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
 
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
      final HandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        final HandlerExecutionChain handler = handlerMapping.getHandler(request);
        assertNotNull("No handler found for request, check you request mapping", handler);

        final Object controller = handler.getHandler();
        // if you want to override any injected attributes do it here

        final HandlerInterceptor[] interceptors =
            handlerMapping.getHandler(request).getInterceptors();
        for (HandlerInterceptor interceptor : interceptors) {
            final boolean carryOn = interceptor.preHandle(request, response, controller);
            if (!carryOn) {
                return null;
            }
        }

        final ModelAndView mav = handlerAdapter.handle(request, response, controller);
        return mav;
    }
     @Test
    public void hello() throws Exception {
         request.setRequestURI("/test");
        request.setMethod("GET");
        final ModelAndView mav = handle(request, response);
         _log.info(response.getStatus());
         assertTrue(true);
   
    }
}
