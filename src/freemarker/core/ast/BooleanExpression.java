package freemarker.core.ast;

import freemarker.core.Environment;
import freemarker.template.*;

abstract class BooleanExpression extends Expression {

    public Object evaluate(Environment env) throws TemplateException 
    {
        return isTrue(env) ? TemplateBooleanModel.TRUE : TemplateBooleanModel.FALSE;
    }
}
