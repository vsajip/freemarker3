package freemarker.ext.beans;

import java.util.AbstractList;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelAdapter;
import freemarker.template.EvaluationException;
import freemarker.template.TemplateSequenceModel;
import freemarker.template.utility.UndeclaredThrowableException;

/**
 * @author Attila Szegedi
 * @version $Id: SequenceAdapter.java,v 1.2 2005/06/12 19:03:04 szegedia Exp $
 */
class SequenceAdapter extends AbstractList implements TemplateModelAdapter {
    private final ObjectWrapper wrapper;
    private final TemplateSequenceModel model;
    
    SequenceAdapter(TemplateSequenceModel model, ObjectWrapper wrapper) {
        this.model = model;
        this.wrapper = wrapper;
    }
    
    public TemplateModel getTemplateModel() {
        return model;
    }
    
    public int size() {
        try {
            return model.size();
        }
        catch(EvaluationException e) {
            throw new UndeclaredThrowableException(e);
        }
    }
    
    public Object get(int index) {
        try {
            return wrapper.unwrap(model.get(index));
        }
        catch(EvaluationException e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}
