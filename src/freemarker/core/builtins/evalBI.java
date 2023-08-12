package freemarker.core.builtins;

import freemarker.core.Environment;
import freemarker.core.ast.BuiltInExpression;
import freemarker.core.ast.Expression;
import freemarker.core.parser.FMLexer;
import freemarker.core.parser.FMParser;
import freemarker.core.parser.ParseException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

import static freemarker.ext.beans.ObjectWrapper.*;

/**
 * Implementation of ?eval built-in 
 */

public class evalBI extends ExpressionEvaluatingBuiltIn {

    @Override
    public boolean isSideEffectFree() {
        return false;
    }

    @Override
    public Object get(Environment env, BuiltInExpression caller, Object model) 
    {
        try {
            return eval(asString(model), env, caller);
        } catch (ClassCastException cce) {
            throw new TemplateModelException("Expecting string on left of ?eval built-in");

        } catch (NullPointerException npe) {
            throw new TemplateModelException(npe);
        }
    }

    Object eval(String s, Environment env, BuiltInExpression caller) 
    {
        String input = "(" + s + ")";
        FMLexer token_source= new FMLexer("input", input, FMLexer.LexicalState.EXPRESSION, caller.getBeginLine(), caller.getBeginColumn());;
        FMParser parser = new FMParser(token_source);
        parser.setTemplate(caller.getTemplate());
        Expression exp = null;
        try {
            exp = parser.Expression();
        } catch (ParseException pe) {
            pe.setTemplateName(caller.getTemplate().getName());
            throw new TemplateException(pe, env);
        }
        return exp.evaluate(env);
    }
}