/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import org.wp.spring.php.PHPScriptFactory;
import org.junit.Ignore;
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
import org.wp.spring.php.PHPContext;
import org.wp.spring.php.PHPEnvironment;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Shishakov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml") // the Spring context file
public class PHPScriptingTest {

    Logger log = Logger.getLogger(PHPScriptingTest.class);
    @Autowired
    @Qualifier("lime")
    private Lime _lime;
    @Autowired
    @Qualifier("limeSuper")
    private Lime _limeSuper;
    @Autowired
    @Qualifier("phpFactory")
    private PHPScriptFactory fact;

    public PHPScriptingTest() {
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
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:

    /**
     * testing lime class
     */
    @Test
    @Ignore
    public void LimeTest() {
        assertNotNull(_lime);
        assertTrue(_lime.getId() > 0);
        assertTrue(_lime.type().equals("LIME"));
        assertFalse(_lime.isEmpty());
        _lime.eatIt();
        assertTrue(_lime.isEmpty());
    }

    /**
     * testing php lime class
     */
    @Test
    @Ignore
    public void LimeSuperTest() {
        assertNotNull(_limeSuper);
        log.info(_limeSuper.toString());
        assertTrue(_limeSuper.getId() > 0);
        assertTrue(_limeSuper.type().equals("LIME"));
        assertFalse(_limeSuper.isEmpty());
        _limeSuper.eatIt();
        assertTrue(_limeSuper.isEmpty());
    }

    private void write(String path, String content) throws IOException {
        File file = new File(path);
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();
    }

    @Test
    @Ignore
    public void LimeScriptTest() {
        String content = "<?php \n"
                + "$empty = False;\n"
                + "$type = \"LIME\";\n"
                + "$id =  \"14\";\n"
                + "function getId() {\n"
                + "    global $id;\n"
                + "    return $id;\n"
                + "}\n"
                + "function type() {\n"
                + "    global $type; \n"
                + "    return $type;\n"
                + "}\n"
                + "function isEmpty() {\n"
                + "    global $empty;\n"
                + "    return $empty;\n"
                + "}\n"
                + "function eatIt() {\n"
                + "    global $empty;\n"
                + "    $empty = True;\n"
                + "}\n"
                + "?>";
        PHPScriptFactory testFact = new PHPScriptFactory();
        PHPContext php = new PHPContext();
        php.init();
        testFact.setQuercusContext(php);
        Class[] is = {Lime.class};

        Lime lime1 = (Lime) testFact.create(content, is);
        Lime lime2 = (Lime) testFact.create(content, is);
        assertTrue(lime1.getId() > 0);
        assertTrue(lime2.getId() > 0);
        lime1.eatIt();
        assertNotSame(lime1.isEmpty(), lime2.isEmpty());

        php.destroy();

    }

    @Test
    // @Ignore
    public void LimeScriptRefreshTest() throws IOException, InterruptedException {
        String tDir = System.getProperty("java.io.tmpdir");
        String content = "<?php class Lime {"
                + " protected $id=14;"
                + " protected $empty=false;\n"
                + " protected $typeb=\"LIME\";\n"
                + " public function __construct(/* ... */) {"
                + "     $this->id =  14; \n"
                + "     $this->typeb=\"LIME\";\n"
                + " }\n"
                + " public function getId() {\n"
                + "     return $this->id;\n"
                + " }\n"
                + " public function type() {\n"
                + "     return $this->typeb;\n"
                + " }\n"
                + " public function isEmpty() {"
                + "     return $this->empty;"
                + " }"
                + " public function eatIt() {"
                + "     $this->empty = true;"
                + " }"
                + "}"
                + "?>";

        String path = tDir + File.separator + "test.php";
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        // write(path, content);

        Class[] is = {Lime.class};
        Lime obj = (Lime) fact.create(path, is, Lime.class);
        // log.info(obj.wantIt());
        assertEquals("LIME", obj.type());

        write(path, content.replace("LIME", "LIMECOOL"));
        Thread.currentThread().sleep(5000);
        //  log.info(obj.wantIt());
        assertEquals("LIMECOOL", obj.type());

        file.delete();
        assertTrue(true);
    }
}
