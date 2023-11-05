package freemarker.core.variables;

/**
 * <p>This is a marker interface that indicates
 * that an object does not need to be "wrapped" as a Pojo.
 * 
 * @see WrappedHash
 * @see WrappedSequence
 * @see WrappedString
 *
 */
public interface WrappedVariable {
    default Object getWrappedObject() {
        return null;
    };
}