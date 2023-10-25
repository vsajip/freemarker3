package freemarker.core.builtins;

import freemarker.core.Environment;
import freemarker.core.nodes.generated.BuiltInExpression;
import freemarker.core.nodes.generated.TemplateNode;
import freemarker.template.*;

/**
 * Implementation of ?resolve built-in 
 */

public abstract class HashBuiltin extends ExpressionEvaluatingBuiltIn {

    @Override
    public Object get(Environment env, BuiltInExpression caller,
            Object model) 
    {
        if (!(model instanceof TemplateHashModel)) {
            throw TemplateNode.invalidTypeException(model, 
                    caller.getTarget(), env, "extended hash");
        }
        final Iterable result = apply((TemplateHashModel) model);
//        if (!(result instanceof WrappedSequence)) {
//            return new CollectionAndSequence(result);
//        } 
        return result;
    }
    
    public abstract Iterable apply(TemplateHashModel hash) 
    throws EvaluationException;
    
    public static class Keys extends HashBuiltin {
        @Override
        public Iterable apply(TemplateHashModel hash)
        {
            return hash.keys();
        }
    }

    public static class Values extends HashBuiltin {
        @Override
        public Iterable apply(TemplateHashModel hash)
        {
            return hash.values();
        }
    }
}
