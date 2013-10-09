/*
 * Copyright (c) 2013 World Page Company -- all rights reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.scripting.php;

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
 * @author Sergej Varjuhin <cepreu.mail@gmail.com>
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//set order for execiting methods
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:org/springframework/scripting/php/test-context.xml") // the Spring context file
public class PhpToJavaTypesTest {
    
    Logger log = Logger.getLogger(PhpToJavaTypesTest.class);
    
    @Autowired
    @Qualifier("primitiveContainer")
    private PrimitiveTypesContainer primitiveContainer;
    
    public PhpToJavaTypesTest() {
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
    public void testTypesGetters() {
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
    public void testTypesSetters() throws MalformedURLException {
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
        Pojo t = (Pojo) primitiveContainer.setObject(new Pojo());
        assertTrue(t.test() == 1);
        assertEquals(primitiveContainer.setCharArray("hello".toCharArray()), "hello");
        assertEquals(primitiveContainer.setByteArray("hello".getBytes()), "hello");
        assertEquals(primitiveContainer.setURL(new URL("http://java.sun.com/index.html")), new URL("http://java.sun.com/index.html"));
        assertEquals(primitiveContainer.setChar("hello".charAt(0)), "h");
    }        
    
}
