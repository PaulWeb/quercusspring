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


import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wp.spring.php.view.PhpContext;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Shishakov <paulandweb@gmail.com>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//set order for execiting methods
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:org/springframework/scripting/php/test-context.xml")
public class PhpScriptingTest {

    Logger log = Logger.getLogger(PhpScriptingTest.class);
    
    @Autowired
    @Qualifier("lime")
    private Lime lime;
    @Autowired
    @Qualifier("inlineLime")
    private Lime inlineLime;

    public PhpScriptingTest() {
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
    public void testPhpObjectCreateFromFile() {
        assertNotNull(this.lime);
        assertTrue(this.lime.getId() > 0);
        assertTrue(this.lime.type().equals("LIME"));
        assertFalse(this.lime.isEmpty());
        this.lime.eatIt();
        assertTrue(this.lime.isEmpty());
    }

    /**
     * helper method for manipulation with file
     * @param path 
     * @param content
     * @throws IOException 
     */
    private void write(String path, String content) throws IOException {
        File file = new File(path);
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();
    }

    @Test
    public void testObjectCreateFromInline() {
        assertTrue(this.inlineLime.getId() == 14);
    }

}
