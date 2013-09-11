package com.programmaticallyspeaking.jz2013;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCIf;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

/**
 * This class extends {@link TreeTranslator} in order to add {@code null} checks
 * in methods for parameters annotated with the {@link NotNull} annotation.
 * Unfortunately we have to keep track of a lot of context manually in order to
 * insert null check statements in method bodies, so we might as well have
 * implemented this using the {@link TreeScanner} class.
 */
public class NullCheckGenerator extends TreeTranslator {

	private final TreeMaker treeMaker;
	private final JavacElements elements;
	private final Messager messager;

	private JCMethodDecl currentMethod;
	private JCBlock currentMethodBody;

	public NullCheckGenerator(Context context, Messager messager) {
		this.messager = messager;
		
		// We use TreeMaker to generate new nodes to insert in the AST.
		this.treeMaker = TreeMaker.instance(context);

		// We use JavacElements to convert strings to Name instances, which
		// are used instead of strings in the various APIs.
		elements = JavacElements.instance(context);
	}

	@Override
	public void visitMethodDef(JCMethodDecl arg0) {
		// Set the method context, and reset once we're done. This is necessary
		// since
		// JCTree instances don't have a parent reference.
		JCMethodDecl oldCurrentMethod = currentMethod;
		currentMethod = arg0;
		super.visitMethodDef(arg0);
		currentMethod = oldCurrentMethod;
	}

	@Override
	public void visitBlock(JCBlock arg0) {
		// Set the method body context, and reset once we're done. This is
		// necessary
		// since JCTree instances don't have a parent reference.
		JCBlock oldCurrentMethodBody = currentMethodBody;
		if (currentMethod != null && currentMethod.body == arg0) {
			currentMethodBody = arg0;
		}
		super.visitBlock(arg0);
		currentMethodBody = oldCurrentMethodBody;
	}

	@Override
	public <T extends JCTree> List<T> translate(List<T> arg0) {
		List<T> trees = super.translate(arg0);
		if (currentMethodBody != null && currentMethodBody.stats == arg0) {
			for (List<JCVariableDecl> l = currentMethod.params; l.nonEmpty(); l = l.tail) {
				JCVariableDecl varDecl = l.head;
				if (JavacUtils.INSTANCE.hasAnnotation(varDecl.getModifiers(),
						NotNull.class)) {
					
					if (varDecl.vartype.getKind() == Tree.Kind.PRIMITIVE_TYPE) {
						messager.printMessage(
								Kind.ERROR,
								String.format(
										"Cannot use @NotNull for parameter '%s' of method '%s' because it has a primitive type.",
										varDecl.getName(),
										currentMethod.getName()),
								varDecl.sym);
						continue;
					}
					
					// Ugly cast needed, but at this point we're translating a
					// list of JCStatements, so it's ok!
					trees = trees.prepend((T) generateNullCheckStatement(varDecl));
					
					// Make a note of the generated null check.
					messager.printMessage(
							Kind.NOTE,
							String.format(
									"Generated null check for parameter '%s' of method '%s'.",
									varDecl.getName(), currentMethod.getName()),
							varDecl.sym);
				}
			}
		}
		return trees;
	}

	private JCStatement generateNullCheckStatement(JCVariableDecl param) {
		// Make sure that the generated code has the same source position as
		// the parameter itself, so that the stack trace of the exception (if
		// thrown) is correct.
		treeMaker.pos = param.pos;

		// Create an identifier that refers to the parameter by name. The
		// created statement will be prepended to all ordinary method
		// statements, so the parameter will be in scope.
		JCIdent ident = treeMaker.Ident(param.getName());

		// BOT is the BOTtom type, or null.
		JCLiteral nullLiteral = JavacUtils.INSTANCE
				.createNullLiteral(treeMaker);

		// Compare the parameter name with null. Note that this assumes that the
		// parameter isn't of a primitive type, but we check that earlier...
		JCBinary paramIsNull = JavacUtils.INSTANCE.createEqualsComparison(
				treeMaker, ident, nullLiteral);

		// From java, select lang. From java.lang, select
		// IllegalArgumentException.
		JCFieldAccess iae = treeMaker.Select(
				treeMaker.Select(
						treeMaker.Ident(elements.getName("java")),
				elements.getName("lang")),
			elements.getName("IllegalArgumentException"));

		// Since java.lang is automatically imported, we could also have used
		// the following must simpler expression, but we would miss out on learning
		// about name selection.
		// JCExpression iae = treeMaker.Ident(elements.getName("IllegalArgumentException"));

		String msg = "Parameter '" + param.getName()
				+ "' is null, but was marked with the @NotNull annotation.";
		JCExpression messageLiteral = treeMaker.Literal(msg);

		// Arg 1 is the enclosing class, but we won't instantiate an inner class.
		// Arg 2 is a list of type parameters (of the enclosing class).
		// Arg 3 is the actual class expression.
		// Arg 4 is a list of arguments to pass to the constructor.
		// Arg 5 is a class body, for creating an anonymous class.
		JCNewClass newIae = treeMaker.NewClass(null,
				List.<JCExpression> nil(), iae,
				List.of(messageLiteral), null);
		JCThrow throwEx = treeMaker.Throw(newIae);

		JCIf ifStatement = treeMaker.If(paramIsNull, throwEx, null); // no else statement
		return ifStatement;
	}
}
