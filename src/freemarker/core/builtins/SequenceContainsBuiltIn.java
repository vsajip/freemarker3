package freemarker.core.builtins;

import java.util.Iterator;
import java.util.List;

import freemarker.core.Environment;
import freemarker.core.ast.BuiltInExpression;
import freemarker.core.parser.ast.BaseNode;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateSequenceModel;

/**
 * @author Attila Szegedi
 * @version $Id: $
 */
public class SequenceContainsBuiltIn extends ExpressionEvaluatingBuiltIn {

    @Override
    public boolean isSideEffectFree() {
        return false; // can depend on locale and arithmetic engine 
    }

    @Override
    public Object get(Environment env, BuiltInExpression caller,
            Object model) 
    {
        if (!(model instanceof TemplateSequenceModel || model instanceof TemplateCollectionModel)) {
            throw BaseNode.invalidTypeException(model, caller.getTarget(), env, "sequence or collection");
        }
        
        return new SequenceContainsFunction(model);
    }

    static class SequenceContainsFunction implements TemplateMethodModelEx {
        final TemplateSequenceModel sequence;
        final TemplateCollectionModel collection;
        SequenceContainsFunction(Object seqModel) {
            if (seqModel instanceof TemplateCollectionModel) {
                collection = (TemplateCollectionModel) seqModel;
                sequence = null;
            }
            else if (seqModel instanceof TemplateSequenceModel) {
                sequence = (TemplateSequenceModel) seqModel;
                collection = null;
            }
            else {
                throw new AssertionError();
            }
        }

        public TemplateModel exec(List args) {
            if (args.size() != 1) {
                throw new TemplateModelException("Expecting exactly one argument for ?seq_contains(...)");
            }
            TemplateModel compareToThis = (TemplateModel) args.get(0);
            final ModelComparator modelComparator = new ModelComparator(Environment.getCurrentEnvironment());
            if (collection != null) {
                Iterator<Object> tmi = collection.iterator();
                while (tmi.hasNext()) {
                    if (modelComparator.modelsEqual(tmi.next(), compareToThis)) {
                        return TemplateBooleanModel.TRUE;
                    }
                }
                return TemplateBooleanModel.FALSE;
            }
            else {
                for (int i=0; i<sequence.size(); i++) {
                    if (modelComparator.modelsEqual(sequence.get(i), compareToThis)) {
                        return TemplateBooleanModel.TRUE;
                    }
                }
                return TemplateBooleanModel.FALSE;
            }
        }
    }
}
