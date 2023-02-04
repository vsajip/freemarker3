package freemarker.ext.beans;

import java.util.Collection;

import freemarker.ext.util.ModelFactory;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelIterator;

/**
 * <p>A special case of {@link BeanModel} that can wrap Java collections
 * and that implements the {@link TemplateCollectionModel} in order to be usable 
 * in a <tt>&lt;foreach></tt> block.</p>
 * @author Attila Szegedi
 * @version $Id: CollectionModel.java,v 1.22 2003/06/03 13:21:32 szegedia Exp $
 */
public class CollectionModel
extends
    StringModel
implements
    TemplateCollectionModel
{
    static final ModelFactory FACTORY =
        new ModelFactory()
        {
            public TemplateModel create(Object object, ObjectWrapper wrapper)
            {
                return new CollectionModel((Collection)object, (BeansWrapper)wrapper);
            }
        };


    /**
     * Creates a new model that wraps the specified collection object.
     * @param collection the collection object to wrap into a model.
     * @param wrapper the {@link BeansWrapper} associated with this model.
     * Every model has to have an associated {@link BeansWrapper} instance. The
     * model gains many attributes from its wrapper, including the caching 
     * behavior, method exposure level, method-over-item shadowing policy etc.
     */
    public CollectionModel(Collection collection, BeansWrapper wrapper)
    {
        super(collection, wrapper);
    }

    public TemplateModelIterator iterator()
    {
        return new IteratorModel(((Collection)object).iterator(), wrapper);
    }
}
