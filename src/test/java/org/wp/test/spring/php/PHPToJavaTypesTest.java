/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wp.test.spring.php;

import com.caucho.quercus.env.ObjectValue;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
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

import com.caucho.quercus.env.Env;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author user
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//set order for execiting methods
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml") // the Spring context file
public class PHPToJavaTypesTest {
    
    Logger log = Logger.getLogger(PHPToJavaTypesTest.class);
    
    @Autowired
    @Qualifier("primitiveContainer")
    private PrimitiveTypesContainer primitiveContainer;
    
    public PHPToJavaTypesTest() {
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
    public void typesGettersTest() {
        assertNotNull(primitiveContainer);
        assertEquals(primitiveContainer.getInt(), 32);
        assertEquals(primitiveContainer.getFloat(), 32.2, 0.0);
        assertEquals(primitiveContainer.getBool(), true);
        assertEquals(primitiveContainer.getString(), "hello");
        assertEquals(primitiveContainer.getArray().get(2), new Long(3)); // new Long() because get returns Object
        assertEquals(primitiveContainer.getNull(), null);
        assertEquals(((ObjectValue) primitiveContainer.getObject()).getClassName(), "testObject");
        assertEquals(primitiveContainer.getMap().get("two"), new Long(2));
    }    
    
    @Test
    public void typesSettersTest() throws MalformedURLException {
        assertNotNull(primitiveContainer);
        assertEquals(primitiveContainer.setNull(null), null);
        assertEquals(primitiveContainer.setBoolean(true), true);
        byte x = 32;
        assertEquals(primitiveContainer.setByte(x), 32);
        short y = 32;
        assertEquals(primitiveContainer.setShort(y), 32);
        assertEquals(primitiveContainer.setInt(32), 32);
        assertEquals(primitiveContainer.setLong(new Long(32)), 32);
        assertEquals(primitiveContainer.setFloat(new Float(32.2)), 32.2, 0.1);
        assertEquals(primitiveContainer.setDouble(32.2), 32.2, 0.0);
        assertEquals(primitiveContainer.setString("hello"), "hello");
        int[] z = {0, 1, 2, 3};
        assertTrue(primitiveContainer.setArray(z).get(0) == z[0]);
        Calendar c = Calendar.getInstance();
        assertTrue(primitiveContainer.setCalendar(c) <= c.getTimeInMillis());
        Date d = new Date();
        assertTrue(primitiveContainer.setDate(d) <= d.getTime());
        assertTrue(primitiveContainer.setList(new ArrayList()).isEmpty());
        assertTrue(primitiveContainer.setMap(new HashMap()).isEmpty());
        TestPOJO t = (TestPOJO) primitiveContainer.setObject(new TestPOJO());
        assertTrue(t.test() == 1);
        assertEquals(primitiveContainer.setCharArray("hello".toCharArray()), "hello");
        assertEquals(primitiveContainer.setByteArray("hello".getBytes()), "hello");
        assertEquals(primitiveContainer.setURL(new URL("http://java.sun.com/index.html")), new URL("http://java.sun.com/index.html"));
        assertEquals(primitiveContainer.setChar("hello".charAt(0)), "h");
    }        
    
}
