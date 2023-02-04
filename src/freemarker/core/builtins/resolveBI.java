package freemarker.core.builtins;

import java.util.List;

import freemarker.core.Scope;
import freemarker.core.Environment;
import freemarker.core.ast.BuiltInExpression;
import freemarker.template.*;

/**
 * Implementation of ?resolve built-in 
 */

public class resolveBI extends ExpressionEvaluatingBuiltIn {

    @Override
    public TemplateModel get(Environment env, BuiltInExpression caller,
            TemplateModel model) 
    throws TemplateException {
        if (!(model instanceof Scope)) {
            throw new TemplateException("Expecting scope on left of ?resolve built-in", env);
        }
        final Scope scope = (Scope) model;
        return new TemplateMethodModel() {
            @Parameters("key")
            public Object exec(List args) throws TemplateModelException {
                return scope.resolveVariable((String) args.get(0)); 
            }
        };
    }
}
