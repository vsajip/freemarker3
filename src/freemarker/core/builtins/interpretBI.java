package freemarker.core.builtins;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.List;

import freemarker.core.Environment;
import freemarker.core.InvalidReferenceException;
import freemarker.core.nodes.generated.BuiltInExpression;
import freemarker.template.Template;
import freemarker.core.variables.UserDirectiveBody;
import freemarker.core.variables.UserDirective;
import freemarker.core.variables.EvaluationException;
import freemarker.template.TemplateException;

import static freemarker.core.variables.Invoke.*;

/**
 * Implementation of ?interpret built-in 
 */
public class interpretBI extends ExpressionEvaluatingBuiltIn {

    @Override
    public Object get(Environment env, BuiltInExpression caller, Object model) 
    {
        String id = null, interpretString = null;
        if (isList(model)) {
            List tsm = asList(model);
            Object tm = tsm.size() >1 ? tsm.get(1) : null;
            if (tm != null) {
                if (isString(tm)) {
                    id = asString(tm);
                }
                else {
                    throw new EvaluationException("Expecting string as second item of sequence of left of ?interpret built-in");
                }
            }
            tm = tsm.get(0);
            if (!isString(tm)) {
                throw new EvaluationException("Expecting string as first item of sequence of left of ?interpret built-in");
            }
            interpretString = asString(tm);
        }
        else if (isString(model)) {
            interpretString = asString(model);
        }
        if (id == null) id = "anonymous_interpreted";
        if (interpretString == null) {
            throw new InvalidReferenceException("No string to interpret", env);
        }
        Template parentTemplate = env.getTemplate();
        try {
            Template template = new Template(parentTemplate.getName() + "$" + id, new StringReader(interpretString), parentTemplate.getConfiguration());
            template.setLocale(env.getLocale());
            return new TemplateProcessorModel(template);
        }
        catch(IOException e) {
            throw new TemplateException("", e, env);
        }
    }

    private static class TemplateProcessorModel implements UserDirective {
        private final Template template;

        TemplateProcessorModel(Template template) {
            this.template = template;
        }

        public void execute(Environment env, Map<String, Object> params,
                Object[] loopVars, UserDirectiveBody body)
                throws IOException {
            try {
                env.include(template, false);
            }
            catch(RuntimeException e) {
                throw e;
            }
            catch(IOException e) {
                throw e;
            }
            catch(Exception e) {
                throw new EvaluationException(e);
            }
        }
    }
}
