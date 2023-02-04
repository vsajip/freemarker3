package freemarker.template;

import java.io.Serializable;


/**
 * A simple implementation of the <tt>TemplateNumberModel</tt>
 * interface. Note that this class is immutable.
 *
 * <p>This class is thread-safe.
 *
 * @author <A HREF="mailto:jon@revusky.com">Jonathan Revusky</A>
 */
public final class SimpleNumber implements TemplateNumberModel, Serializable {
    private static final long serialVersionUID = -9151122919191390917L;

    /**
     * @serial the value of this <tt>SimpleNumber</tt> 
     */
    private Number value;

    public SimpleNumber(Number value) {
        this.value = value;
    }

    public SimpleNumber(byte val) {
        this.value = Byte.valueOf(val);
    }

    public SimpleNumber(short val) {
        this.value = Short.valueOf(val);
    }

    public SimpleNumber(int val) {
        this.value = Integer.valueOf(val);
    }

    public SimpleNumber(long val) {
        this.value = Long.valueOf(val);
    }

    public SimpleNumber(float val) {
        this.value = new Float(val);
    }
    
    public SimpleNumber(double val) {
        this.value = new Double(val);
    }

    public Number getAsNumber() {
        return value;
    }

    public String toString() {
        return value.toString();
    }
}
