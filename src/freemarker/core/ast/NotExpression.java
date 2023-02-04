package freemarker.core.ast;

import freemarker.core.Environment;
import freemarker.template.TemplateException;

public class NotExpression extends BooleanExpression {

    private Expression target;

    public NotExpression(Expression target) {
        this.target = target;
        target.parent = this;
    }
    
    public Expression getTarget() {
    	return target;
    }

    boolean isTrue(Environment env) throws TemplateException {
        return (!target.isTrue(env));
    }

    boolean isLiteral() {
        return target.isLiteral();
    }

    Expression _deepClone(String name, Expression subst) {
    	return new NotExpression(target.deepClone(name, subst));
    }
}
