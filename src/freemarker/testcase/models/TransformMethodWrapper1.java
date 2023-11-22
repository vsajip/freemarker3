package freemarker.testcase.models;

import freemarker.core.variables.WrappedMethod;
import freemarker.template.utility.HtmlEscape;
import freemarker.template.utility.XmlEscape;

/**
 * Simple test of the interaction between MethodModels and TransformModels.
 *
 * @version $Id: TransformMethodWrapper1.java,v 1.12 2004/01/06 17:06:44 szegedia Exp $
 */
public class TransformMethodWrapper1 extends Object implements WrappedMethod {

    public Object exec(Object... arguments) {

        if (arguments.length > 0 && arguments[0].toString().equals("xml")) {
            return new XmlEscape();
        } else {
            return new HtmlEscape();
        }
    }
}
