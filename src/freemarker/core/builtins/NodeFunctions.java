package freemarker.core.builtins;

import java.util.List;

import freemarker.core.Environment;
import freemarker.core.nodes.generated.BuiltInExpression;
import freemarker.core.nodes.generated.TemplateNode;
import freemarker.ext.beans.ListModel;
import freemarker.template.TemplateMethodModel;
import freemarker.template.WrappedNode;
import freemarker.template.Constants;
import freemarker.template.utility.StringUtil;

/**
 * Implementations of ?children, ?node_name, and other 
 * standard functions that operate on nodes
 */

public abstract class NodeFunctions extends ExpressionEvaluatingBuiltIn {

    @Override
    public Object get(Environment env, BuiltInExpression caller, Object model) 
    {
        if (!(model instanceof WrappedNode)) {
            throw TemplateNode.invalidTypeException(model, caller.getTarget(), env, "node");
        }
        return apply(env, (WrappedNode)model);
    }

    public abstract Object apply(Environment env, WrappedNode node);
    
    public static class Parent extends NodeFunctions {
        @Override
        public Object apply(Environment env, WrappedNode node)
        {
            return node.getParentNode();
        }
    }

    public static class Children extends NodeFunctions {
        @Override
        public Object apply(Environment env, WrappedNode node)
        {
            return node.getChildNodes();
        }
    }

    public static class Root extends NodeFunctions {
        @Override
        public Object apply(Environment env, WrappedNode node)
        {
            for(;;) {
                final WrappedNode parent = node.getParentNode();
                if(parent == null) {
                    return node;
                }
                node = parent;
            }
        }
    }
    
    public static class NodeName extends NodeFunctions {
        @Override
        public Object apply(Environment env, WrappedNode node)
        {
            return node.getNodeName();
        }
    }

    public static class NodeNamespace extends NodeFunctions {
        @Override
        public Object apply(Environment env, WrappedNode node)
        {
            String ns = node.getNodeNamespace();
            return ns == null ? Constants.JAVA_NULL : ns;
        }
    }

    public static class NodeType extends NodeFunctions {
        @Override
        public Object apply(Environment env, WrappedNode node)
        {
            String nt = node.getNodeType();
            return nt == null ? Constants.JAVA_NULL : nt;
        }
    }
    

    public static class Ancestors extends NodeFunctions {
        @Override
        public Object apply(Environment env, WrappedNode node)
        {
            final AncestorSequence result = new AncestorSequence();
            WrappedNode parent = node.getParentNode();
            while (parent != null) {
                result.add(parent);
                parent = parent.getParentNode();
            }
            return result;
        }
    }

    static class AncestorSequence extends ListModel implements TemplateMethodModel {
        public Object exec(List names) {
            if (names == null || names.isEmpty()) {
                return this;
            }
            AncestorSequence result = new AncestorSequence();
            final Environment env = Environment.getCurrentEnvironment();
            for (int i=0; i<size(); i++) {
                WrappedNode tnm = (WrappedNode) get(i);
                String nodeName = tnm.getNodeName();
                String nsURI = tnm.getNodeNamespace();
                if (nsURI == null) {
                    if (names.contains(nodeName)) {
                        result.add(tnm);
                    }
                } else {
                    for (int j = 0; j<names.size(); j++) {
                        if (StringUtil.matchesName((String) names.get(j), nodeName, nsURI, env)) {
                            result.add(tnm);
                            break;
                        }
                    }
                }
            }
            return result;
        }
    }	
}
