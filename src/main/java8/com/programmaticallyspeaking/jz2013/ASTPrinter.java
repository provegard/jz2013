package com.programmaticallyspeaking.jz2013;

import javax.annotation.processing.Messager;
import com.sun.tools.javac.tree.JCTree.JCLambda;
import com.sun.tools.javac.tree.JCTree.JCMemberReference;

/**
 * AST printer for Java 8, in order to print AST nodes for lambda exressions and
 * member references.
 */
public class ASTPrinter extends ASTPrinter7 {

	public ASTPrinter(Messager messager) {
		super(messager);
	}

	@Override
	public void visitLambda(JCLambda tree) {
		print(tree, "LAMBDA, paramkind = %s", tree.paramKind);
		// targets
		// public boolean canCompleteNormally = true;
		// public List<Type> inferredThrownTypes;
		// public ParameterKind paramKind;
		super.visitLambda(tree);
	}

	@Override
	public void visitReference(JCMemberReference tree) {
		print(tree, "MEMBERREF, name = %s, sym = %s, refkind = %s",
				tree.getName(), describe(tree.sym), tree.kind);
		// targets
		// public ReferenceMode mode;
		// public ReferenceKind kind;
		// public Name name;
		// public JCExpression expr;
		// public List<JCExpression> typeargs;
		// public Symbol sym;
		// public Type varargsElement;
		// public PolyKind refPolyKind;
		// public boolean ownerAccessible;
		super.visitReference(tree);
	}
}
