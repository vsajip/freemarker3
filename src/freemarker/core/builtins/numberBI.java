package freemarker.core.builtins;

import freemarker.core.Environment;
import freemarker.core.ArithmeticEngine;
import freemarker.core.nodes.generated.BuiltInExpression;
import freemarker.core.nodes.generated.TemplateNode;
import freemarker.template.TemplateException;

import static freemarker.core.evaluation.ObjectWrapper.*;

public class numberBI extends ExpressionEvaluatingBuiltIn
{
    @Override
    public Object get(Environment env, BuiltInExpression caller,
            Object value) throws TemplateException
    {
        if(isNumber(value)) {
            return value;
        }
        final String string;
        try {
            string = asString(value);
        }
        catch(ClassCastException ex) {
            throw TemplateNode.invalidTypeException(value, caller.getTarget(), env, "string or number");
        }
        ArithmeticEngine e = env == null ? caller.getTemplate().getArithmeticEngine() : env.getArithmeticEngine();
        try {
            //return wrap(e.toNumber(string));
            return e.toNumber(string);
        } catch(NumberFormatException nfe) {
                String mess = "Error: " + caller.getLocation()
                + "\nExpecting a number in string here, found: " + string;
                throw new TemplateException(mess, env);
            }
    }
}