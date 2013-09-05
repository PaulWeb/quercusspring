/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wp.test.spring.php;

import com.caucho.quercus.env.ObjectValue;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author user
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//set order for execiting methods
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:xsd-test-context.xml") // the Spring context file

public class PHPXmlHandlerTest {

    @Autowired
    private Lime lime;
    @Autowired
    private Fruit fruit;
    
    public PHPXmlHandlerTest() {
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
    public void getLimeTest() {
        assertNotNull(lime);
        assertTrue(lime.getId() > 0);
        assertTrue(lime.type().equals("LIME"));
    }
    
    @Test
    public void inlineCodeTest() {
        assertNotNull(fruit);
        int id = fruit.getId();
        assertTrue(id == 12);
    }
    
}
