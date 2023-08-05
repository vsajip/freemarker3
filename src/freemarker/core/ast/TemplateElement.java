package freemarker.core.ast;

import java.util.*;
import java.io.IOException;
import freemarker.template.*;
import freemarker.core.*;



/**
 * Objects that represent elements in the compiled 
 * tree representation of the template necessarily 
 * descend from this abstract class.
 */
abstract public class TemplateElement extends TemplateNode {
	
    TemplateElement nestedBlock;

    List<TemplateElement> nestedElements;
    
    // The scoped variables defined in this element.
    
    HashSet<String> declaredVariables;

    /**
     * Processes the contents of this <tt>TemplateElement</tt> and
     * outputs the resulting text
     *
     * @param env The runtime environment
     */
    abstract public void execute(Environment env) throws TemplateException, IOException;

    public Scope createLocalScope(Scope enclosingScope) {
    	return new BlockScope(this, enclosingScope);
    }
    
    public TemplateElement getParent() {
    	return (TemplateElement) parent;
    }
    
    public boolean declaresVariable(String name) {
    	return declaredVariables != null && declaredVariables.contains(name);
    }
    
    public void declareVariable(String varName) {
    	if (declaredVariables == null) declaredVariables = new HashSet<String>();
    	declaredVariables.add(varName);
    }
    
    public TemplateElement getNestedBlock() {
    	return nestedBlock;
    }
    
    public List<TemplateElement> getNestedElements() {
        return nestedElements;
    }
    
    public void setNestedBlock(TemplateElement nestedBlock) {
    	this.nestedBlock = nestedBlock;
    }
    
    public void setParent(TemplateElement parent) {
    	this.parent = parent;
    }
    

    public TemplateSequenceModel getChildNodes() {
        if (nestedElements != null) {
            return new SimpleSequence(nestedElements);
        }
        SimpleSequence result = new SimpleSequence();
        if (nestedBlock != null) {
            result.add(nestedBlock);
        } 
        return result;
    }
    
    public void setParentRecursively(TemplateElement parent) {
        this.parent = parent;
        int nestedSize = nestedElements == null ? 0 : nestedElements.size();
        for (int i = 0; i < nestedSize; i++) {
        	nestedElements.get(i).setParentRecursively(this);
        }
        if (nestedBlock != null) {
            nestedBlock.setParentRecursively(this);
        }
    }

    public boolean isIgnorable() {
        return false;
    }
    
    public void removeIgnorableChildren() {
    	if (nestedElements != null) {
    		Iterator<TemplateElement> it = nestedElements.iterator();
    		while (it.hasNext()) {
    			TemplateElement child = it.next();
    			if (child.isIgnorable()) it.remove();
    		}
    		if (nestedElements instanceof ArrayList) {
    			((ArrayList) nestedElements).trimToSize();
    		}
    	}
    	else if (nestedBlock != null) {
    		if (nestedBlock.isIgnorable()) {
    			nestedBlock = null;
    		}
    	}
    }

// The following methods exist to support some fancier tree-walking 
// and were introduced to support the whitespace cleanup feature in 2.2

    TemplateElement prevTerminalNode() {
        TemplateElement prev = previousSib();
        if (prev != null) {
            return prev.getLastLeaf();
        }
        else if (parent != null) {
            return getParent().prevTerminalNode();
        }
        return null;
    }

    protected TemplateElement nextTerminalNode() {
        TemplateElement next = nextSib();
        if (next != null) {
            return next.getFirstLeaf();
        }
        else if (parent != null) {
            return getParent().nextTerminalNode();
        }
        return null;
    }



    protected TemplateElement previousSib() {
        if (parent == null) {
            return null;
        }
        TemplateElement parentElement = (TemplateElement) this.parent;
        List<TemplateElement> siblings = parentElement.nestedElements;
        if (siblings == null) {
            return null;
        }
        for (int i = siblings.size() - 1; i>=0; i--) {
            if (siblings.get(i) == this) {
                return(i >0) ? siblings.get(i-1) : null;
            }
        }
        return null;
    }

    protected TemplateElement nextSib() {
        if (parent == null) {
            return null;
        }
        TemplateElement parent = (TemplateElement) this.parent;
        List<TemplateElement> siblings = parent.nestedElements;
        if (siblings == null) {
            return null;
        }
        for (int i = 0; i < siblings.size(); i++) {
            if (siblings.get(i) == this) {
                return (i+1) < siblings.size() ? (TemplateElement) siblings.get(i+1) : null;
            }
        }
        return null;
    }

    private TemplateElement firstChild() {
        if (nestedBlock != null) {
            return nestedBlock;
        }
        if (nestedElements != null && nestedElements.size() >0) {
            return nestedElements.get(0);
        }
        return null;
    }

    private TemplateElement lastChild() {
        if (nestedBlock != null) {
            return nestedBlock;
        }
        if (nestedElements != null && nestedElements.size() >0) {
            return nestedElements.get(nestedElements.size() -1);
        }
        return null;
    }
    
    private boolean isLeaf() {
    	return nestedBlock == null && (nestedElements == null || nestedElements.isEmpty());
    }
    

    public int getIndex(TemplateElement node) {
        if (nestedBlock instanceof MixedContent) {
            return nestedBlock.getIndex(node);
        }
        if (nestedBlock != null) {
            if (node == nestedBlock) {
                return 0;
            }
        }
        else if (nestedElements != null) {
            return nestedElements.indexOf(node);
        }
        return -1;
    }

    public int getChildCount() {
        if (nestedBlock instanceof MixedContent) {
            return nestedBlock.getChildCount();
        }
        if (nestedBlock != null) {
            return 1;
        }
        else if (nestedElements != null) {
            return nestedElements.size();
        }
        return 0;
    }
    
    static final Enumeration EMPTY_ENUMERATION = new Enumeration() {
    	public boolean hasMoreElements() {
    		return false;
    	}
    	
    	public Object nextElement() {
    		throw new NoSuchElementException();
    	}
    };

    public Enumeration childrenE() {
        if (nestedBlock instanceof MixedContent) {
            return nestedBlock.childrenE();
        }
        if (nestedBlock != null) {
            return Collections.enumeration(Collections.singletonList(nestedBlock));
        }
        else if (nestedElements != null) {
            return Collections.enumeration(nestedElements);
        }
        return EMPTY_ENUMERATION;
    }

    public TemplateElement getChildAt(int index) {
        if (nestedBlock instanceof MixedContent) {
            return nestedBlock.getChildAt(index);
        }
        if (nestedBlock != null) {
            if (index == 0) {
                return nestedBlock;
            }
            throw new ArrayIndexOutOfBoundsException("invalid index");
        }
        else if (nestedElements != null) {
            return nestedElements.get(index);
        }
        throw new ArrayIndexOutOfBoundsException("element has no children");
    }

    public void setChildAt(int index, TemplateElement element) {
        if(nestedBlock instanceof MixedContent) {
            nestedBlock.setChildAt(index, element);
        }
        else if(nestedBlock != null) {
            if(index == 0) {
                nestedBlock = element;
                element.parent = this;
            }
            else {
                throw new IndexOutOfBoundsException("invalid index");
            }
        }
        else if(nestedElements != null) {
            nestedElements.set(index, element);
            element.parent = this;
        }
        else {
            throw new IndexOutOfBoundsException("element has no children");
        }
    }    


    private TemplateElement getFirstLeaf() {
        TemplateElement te = this;
        while (!te.isLeaf() && !(te instanceof Macro) && !(te instanceof BlockAssignment)) {
             // A macro or macro invocation is treated as a leaf here for special reasons
            te = te.firstChild();
        }
        return te;
    }

    private TemplateElement getLastLeaf() {
        TemplateElement te = this;
        while (!te.isLeaf() && !(te instanceof Macro) && !(te instanceof BlockAssignment)) {
            // A macro or macro invocation is treated as a leaf here for special reasons
            te = te.lastChild();
        }
        return te;
    }

    public boolean createsScope() {
    	return declaredVariables != null && !declaredVariables.isEmpty();
    }
    
    public Macro getEnclosingMacro() {
        TemplateElement parent = this;
        while (parent != null) {
            parent = parent.getParent();
            if (parent instanceof Macro) {
                return (Macro) parent;
            }
        }
        return null;
    }
    
    /**
     * Replace the child element prev with the current
     * @param prev
     * @param current
     */
    
    public void replace(TemplateNode prev, TemplateElement current) {
    	if (nestedBlock != null) {
    		if (prev == nestedBlock) {
    			nestedBlock = current;
    		}
    		current.parent = this;
    	} 
    	else if (nestedElements != null) {
    		for (int i=0; i<nestedElements.size(); i++) {
    			TemplateNode nestedElement = nestedElements.get(i);
    			if (nestedElement == prev) {
    				nestedElements.set(i, current);
    				current.parent = this;
    			}
    		}
    	}
    }
}
