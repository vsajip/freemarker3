package freemarker.builtins;

import freemarker.core.Environment;
import freemarker.core.nodes.generated.BuiltInExpression;
import freemarker.core.nodes.generated.TemplateNode;
import freemarker.core.variables.WrappedHash;
import static freemarker.core.variables.Wrap.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * Implementation of ?resolve built-in 
 */

public abstract class HashBuiltin extends ExpressionEvaluatingBuiltIn {

    @Override
    public Object get(Environment env, BuiltInExpression caller,
            Object model) 
    {
        if (!(model instanceof WrappedHash) && !isMap(model)) {
            throw TemplateNode.invalidTypeException(model, 
                    caller.getTarget(), env, "hash");
        }
        return apply(unwrap(model));
    }
    
    public abstract Iterable apply(Object hash); 
    
    public static class Keys extends HashBuiltin {
        @Override
        public Iterable apply(Object hash) {
            if (hash instanceof Map) {
                return new ArrayList(((Map)hash).keySet());
            }
            return ((WrappedHash) hash).keys();
        }
    }

    public static class Values extends HashBuiltin {
        @Override
        public Iterable apply(Object hash)
        {
            if (hash instanceof WrappedHash) {
                return ((WrappedHash)hash).values();
            }
            return new ArrayList(((Map) hash).values());
        }
    }
}