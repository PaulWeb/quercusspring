/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wp.test.spring.php;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

/**
 * 
 * @author Paul Shishakov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:test-dispetcher.xml"})
public class PHPResolverTest {
    Logger _log = Logger.getLogger(PHPResolverTest.class);
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    MockHttpServletRequest request;
    @Autowired
    MockHttpServletResponse response;
    private MockMvc mockMvc;
    
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
        this.mockMvc = webAppContextSetup(this.wac).build();
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testMyController() throws Exception {
        MvcResult mr = mockMvc.perform(get("/test").accept(MediaType.TEXT_HTML)).andReturn();        
        assertTrue(mr.getResponse().getStatus() == 200);
        _log.info(mr.getResponse().getContentAsString());
        assertTrue(mr.getResponse().getContentAsString().contains("PHP and SPRING"));
    }
}
