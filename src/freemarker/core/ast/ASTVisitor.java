package freemarker.core.ast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import freemarker.core.parser.ast.TemplateNode;
import freemarker.template.Template;


/**
 * A useful base class for writing tree-walking utilities
 * that walk the AST in a top-down manner.
 * 
 * The base implementations of visit(....) simply invoke
 * visit(...) on the subnodes (or do nothing if the node is terminal.)
 * 
 * For some simple examples, see 
 * {@link freemarker.template.utility.HTMLEncodingASTVisitor}
 * or
 * {@link freemarker.template.utility.PickyPunctuationASTVisitor}
 * 
 * For more complex examples, see: 
 * {@link freemarker.core.helpers.DefaultTreeDumper} or
 * {@link freemarker.template.PostParseVisitor}
 * 
 * If your ASTVisitor implementation maintains state, and hence,
 * is not thread-safe, you should have it implement Cloneable.
 * Code that takes an ASTVisitor object should check
 * if it is an instance of Cloneable, and if so, clone
 * new instance to use.
 * 
 * @author Jonathan Revusky
 */

public abstract class ASTVisitor {
	
	protected StringBuilder errors = new StringBuilder(), warnings = new StringBuilder();
	
	public void visit(TemplateNode node) {
		if (node == null) return;
    	try {
    		Class<? extends TemplateNode> clazz = node.getClass();
        	Method visitMethod = this.getClass().getMethod("visit", new Class[] {clazz});
    		visitMethod.invoke(this, new Object[] {node});
    	}
    	catch (InvocationTargetException ite) {
    		Throwable cause = ite.getCause();
    		if (cause instanceof RuntimeException) {
    			throw (RuntimeException) cause;
    		}
    		throw new RuntimeException(ite);
    	}
    	catch (NoSuchMethodException nsme) {
    		if (node instanceof TemplateElement) {
    			recurse((TemplateElement) node);
    		}
    	}
    	catch (IllegalAccessException e) {
    		throw new RuntimeException(e.getMessage());
    	}
	}
	
	public void visit(Template template) {
		TemplateHeaderElement header = template.getHeaderElement();
		if (header != null) visit(header);
		visit(template.getRootTreeNode());
	}
	
	public void visit(TemplateHeaderElement node) {
		if (node == null) return;
		Map<String,Expression> params = node.getParams();
		for (Expression exp : params.values()) {
			visit(exp);
		}
	}
	
	
	public void visit(AddConcatExpression node) {
		visit(node.getLeft());
		visit(node.getRight());
	}
	
	public void visit(AndExpression node) {
		visit(node.getLeft());
		visit(node.getRight());
	}
	
	public void visit(ArithmeticExpression node) {
		visit(node.getLeft());
		visit(node.getRight());
	}
	
	public void visit(AssignmentInstruction node) {
		for (Expression e : node.getValues()) {
			visit(e);
		}
	}
	
	public void visit(AttemptBlock node) {
		recurse(node);
	}
	
	public void visit(BlockAssignment node) {
		recurse(node);
	}
	
	public void visit(BodyInstruction node) {
		visit(node.getArgs());
	}
	
	public void visit(BuiltInExpression node) {
		visit(node.getTarget());
	}
	
	
	public void visit(BooleanExpression node) {}
	
	
	public void visit(BreakInstruction node) {}
	
	public void visit(BuiltinVariable node) {}
	
	public void visit(Case node) {
		visit(node.getExpression());
		recurse(node);
	}
	
	public void visit(Comment node) {}
	
	public void visit(ComparisonExpression node) {
		visit(node.getLeft());
		visit(node.getRight());
	}
	
	public void visit(CompressedBlock node) {
		recurse(node);
	}
	
	public void visit(ConditionalBlock node) {
		visit(node.getCondition());
		recurse(node);
	}
	
	public void visit(DefaultToExpression node) {
		visit(node.getLeft());
		if (node.getRight() != null) {
			visit(node.getRight());
		}
	}
	
	public void visit(Interpolation node) {
		visit(node.getExpression()); // Do we visit both?
		visit(node.getEscapedExpression());
	}
	
	public void visit(Dot node) {
		visit(node.getTarget());
	}
	
	public void visit(DynamicKeyName node) {
		visit(node.getTarget());
		visit(node.getNameExpression());
	}
	
	public void visit(EscapeBlock node) {
		visit(node.getExpression());
		visit(node.getEscapedExpression());
		recurse(node);
	}
	
	public void visit(ExistsExpression node) {
		visit(node.getExpression());
	}
	
	public void visit(FallbackInstruction node) {}
	
	public void visit(FlushInstruction node) {}
	
	public void visit(HashLiteral node) {
		List<Expression> keys = node.getKeys();
		List<Expression> values = node.getValues();
		for (int i=0; i< keys.size(); i++) {
			visit(keys.get(i));
			visit(values.get(i));
		}
	}
	
	public void visit(Identifier node) {}
	
	public void visit(IfBlock node) {
		recurse(node);
	}
	
	public void visit(Include node) {
		visit(node.getIncludedTemplateExpression());
		if (node.getParseExp() != null) {
			visit(node.getParseExp());
		}
	}

	public void visit(InvalidExpression node) {		
	}
	
	public void visit(IteratorBlock node) {
		visit(node.getListExpression());
		recurse(node);
	}
	
	public void visit(LibraryLoad node) {
		visit(node.getTemplateNameExpression());
	}
	
	public void visit(ListLiteral node) {
		for (Expression exp : node.getElements()) {
			visit(exp);
		}
	}
	
	
	public void visit(Macro node) {
		visit(node.getParams());
		recurse(node);
	}
	
	public void visit(MethodCall node) {
		visit(node.getTarget());
		visit(node.getArgs());
	}
	
	public void visit(MixedContent node) {
		recurse(node);
	}
	
	public void visit(NamedArgsList node) {
		for (Expression exp : node.getArgs().values()) {
			visit(exp);
		}
	}
	
	public void visit(NoEscapeBlock node) {
		recurse(node);
	}
	
	public void visit(NoParseBlock node) {
		recurse(node);
		
	}
	
	public void visit(NotExpression node) {
		visit(node.getTarget());
	}
	
	public void visit(NullLiteral node) {	}
	
	public void visit(NumberLiteral node) {}
	
	public void visit(NumericalOutput node) {
		visit(node.getExpression());
	}
	
	public void visit(OrExpression node) {
		visit(node.getLeft());
		visit(node.getRight());
	}
	
	public void visit(ParameterList node) {
		for (String paramName : node.getParamNames()) {
			Expression defaultExp = node.getDefaultExpression(paramName);
			if (defaultExp != null) {
				visit(defaultExp);
			}
		}
	}
	
	public void visit(ParentheticalExpression node) {
		visit(node.getNested());
	}
	
	
	public void visit(PositionalArgsList node) {
		for (Expression exp : node.args) {
			visit(exp);
		}
	}
	
	public void visit(PropertySetting node) {
		visit(node.getValue());
	}
	
	public void visit(Range node) {
		visit(node.getLeft());
		if (node.getRight() != null) visit(node.getRight());
	}
	
	public void visit(RecoveryBlock node) {
		recurse(node);
	}
	
	public void visit(RecurseNode node) {
		visit(node.getTargetNode());
		visit(node.getNamespaces());
	}
	
	public void visit(ReturnInstruction node) {
		visit(node.returnExp);
	}
	
	public void visit(VarDirective node) {
		for (Expression value : node.getVariables().values()) {
			visit(value);
		}
	}
	
	public void visit(StopInstruction node) {}
	
	public void visit(StringLiteral node) {}
	
	public void visit(SwitchBlock node) {
		visit(node.getTestExpression());
		recurse(node);
	}
	
	public void visit(TextBlock node) {}
	
	public void visit(TransformBlock node) {
		visit(node.getTransformExpression());
		if (node.namedArgs != null) {
			for (Expression exp : node.namedArgs.values()) {
				visit(exp);
			}
		}
		recurse(node);
	}
	
	public void visit(TrimBlock node) {
		visit(node.firstChildOfType(TemplateElement.class));
	}

	public void visit(TrimInstruction node) {}
	
	public void visit(UnaryPlusMinusExpression node) {
		visit(node.getTarget());
	}
	
	public void visit(UnifiedCall node) {
		visit(node.getNameExp());
		if (node.getArgs() != null) {
			visit(node.getArgs());
		}
		if (node.getBodyParameters() != null) {
			visit(node.getBodyParameters());
		}
		recurse(node);
	}
	
	public void visit(VisitNode node) {
		visit(node.getTargetNode());
		if (node.getNamespaces() != null) visit(node.getNamespaces());
	}
	
	protected void recurse(TemplateElement node) {
		for (TemplateElement te : node.childrenOfType(TemplateElement.class)) {
			visit(te);
		}
	}
	
	public ASTVisitor clone() {
		try {
			return (ASTVisitor) super.clone();
		} catch (CloneNotSupportedException cse) {
			throw new IllegalStateException("You tried to clone a visitior implementation that was does not implement cloneable");
		}
	}
}
