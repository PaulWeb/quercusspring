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
 * @author Sergej Varjuhin <cepreu.mail@gmail.com>
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//set order for execiting methods
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:org/springframework/scripting/php/test-context.xml") // the Spring context file

public class PhpJavaFunctionCallTest {

    @Autowired
    private JavaCallTest phpClassUsingJava;
    
    public PhpJavaFunctionCallTest() {
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
    public void testJavaFunctionCall() {
        assertNotNull(phpClassUsingJava);
        String testResult = phpClassUsingJava.getResult();
        assertTrue(testResult.length()>0);
    }
    
}