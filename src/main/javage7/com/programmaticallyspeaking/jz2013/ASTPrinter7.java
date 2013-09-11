package com.programmaticallyspeaking.jz2013;

import javax.annotation.processing.Messager;

import com.sun.tools.javac.tree.JCTree.JCTypeUnion;
import com.sun.tools.javac.tree.JCTree.TypeBoundKind;

/**
 * AST printer for Java 7+. Java 7 introduced type unions in try-catch
 * statements. Type-bound kinds (extends, super etc. with type parameters) have
 * not been exposed in all Java 6 versions.
 */
public class ASTPrinter7 extends ASTPrinterBase {

	public ASTPrinter7(Messager messager) {
		super(messager);
	}

	@Override
	public void visitTypeBoundKind(TypeBoundKind arg0) {
		print(arg0, "TYPEBOUNDKIND, kind = %s", arg0.kind);
		super.visitTypeBoundKind(arg0);
	}

	@Override
	public void visitTypeUnion(JCTypeUnion arg0) {
		print(arg0, "TYPEUNION");
		super.visitTypeUnion(arg0);
	}
}
