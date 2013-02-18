/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul
 * @see http://tedyoung.me/2011/02/14/spring-mvc-integration-testing-controllers/
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:test-dispetcher.xml", loader=MockWebApplicationContextLoader.class)
@MockWebApplication(name="some-controller",webapp="classpath:/")
public class ViewRenderTest  {
    @Autowired
    private DispatcherServlet servlet;
    
    public ViewRenderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    @Test
public void viewTest() throws ServletException, IOException {
	MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
	//request.addParameter("id", "0");
	MockHttpServletResponse response = new MockHttpServletResponse();
	servlet.service(request, response);
	String results = response.getContentAsString().trim().replace("\r", "").replace("\n", "");
        System.out.println(results);
        
        assertNotNull(results);
	assertEquals("<html><body>PHP and SPRING</body></html>PHP", results);
}

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
