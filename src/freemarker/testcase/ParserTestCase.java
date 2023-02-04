package freemarker.testcase;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import freemarker.core.parser.ParseException;
import freemarker.template.Configuration;

/**
 * Test case for parser. The contents of the specified file is supposed to cause
 * a ParseException when parsed by the FMParser.
 * 
 * @version $Id$
 */
public class ParserTestCase extends TestCase {

    private String filename;

    private Configuration conf = new Configuration();

    public ParserTestCase(String name, String filename) throws IOException {
        super(name);
        this.filename = filename;
        File dir = new File("src/freemarker/testcase/template"); 
        conf.setDirectoryForTemplateLoading(dir);
    }

    protected void runTest() throws Throwable {
        try {
            conf.getTemplate(filename);
            fail("ParseException expected.");
        } catch (ParseException pe) {
            return;
        }
    }

}
