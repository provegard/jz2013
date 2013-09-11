package com.programmaticallyspeaking.jz2013;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCArrayAccess;
import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
import com.sun.tools.javac.tree.JCTree.JCAssert;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCAssignOp;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCBreak;
import com.sun.tools.javac.tree.JCTree.JCCase;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCContinue;
import com.sun.tools.javac.tree.JCTree.JCDoWhileLoop;
import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCForLoop;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCIf;
import com.sun.tools.javac.tree.JCTree.JCInstanceOf;
import com.sun.tools.javac.tree.JCTree.JCLabeledStatement;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCParens;
import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCSkip;
import com.sun.tools.javac.tree.JCTree.JCSwitch;
import com.sun.tools.javac.tree.JCTree.JCSynchronized;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.tree.JCTree.JCTry;
import com.sun.tools.javac.tree.JCTree.JCTypeApply;
import com.sun.tools.javac.tree.JCTree.JCTypeCast;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.JCTree.JCWhileLoop;
import com.sun.tools.javac.tree.JCTree.JCWildcard;
import com.sun.tools.javac.tree.JCTree.LetExpr;
import com.sun.tools.javac.tree.TreeScanner;

public class ASTPrinterBase extends TreeScanner {
    private int indent;
    private boolean doPrint = false;
	private final Messager messager;
    
    protected ASTPrinterBase(Messager messager) {
		this.messager = messager;
    }

    protected String describe(Type t) {
    	if (t == null)
    		return "[no type info available]";
        return t.tsym != null ? t.tsym.getQualifiedName().toString()
                : "[tag = " + JavacUtils.INSTANCE.describeTypeTag(t) + "]";
    }
    
    protected String describe(Symbol s) {
        return s != null ? "[sym] " + s.toString() : "[no symbol info available]";
    }

    @Override
    public void visitAnnotation(JCAnnotation arg0) {
        print(arg0, "ANNOTATION");
        super.visitAnnotation(arg0);
    }

    @Override
    public void visitApply(JCMethodInvocation arg0) {
        print(arg0, "METHOD INVOCATION");
        super.visitApply(arg0);
    }

    @Override
    public void visitAssert(JCAssert arg0) {
        print(arg0, "ASSERT");
        super.visitAssert(arg0);
    }

    @Override
    public void visitAssign(JCAssign arg0) {
        print(arg0, "ASSIGN");
        super.visitAssign(arg0);
    }

    @Override
    public void visitAssignop(JCAssignOp arg0) {
        print(arg0, "ASSIGNOP, operators = %s", describe(arg0.getOperator()));
        super.visitAssignop(arg0);
    }

    @Override
    public void visitBinary(JCBinary arg0) {
        print(arg0, "BINARY, tag = %s, operator = %s", JavacUtils.INSTANCE.describeTreeTag(arg0), 
        		describe(arg0.getOperator()));
        super.visitBinary(arg0);
    }

    @Override
    public void visitBlock(JCBlock arg0) {
        print(arg0, "BLOCK");
        super.visitBlock(arg0);
    }

    @Override
    public void visitBreak(JCBreak arg0) {
        print(arg0, "BREAK, label = %s", arg0.label);
        super.visitBreak(arg0);
    }

    @Override
    public void visitCase(JCCase arg0) {
        print(arg0, "CASE");
        super.visitCase(arg0);
    }

    @Override
    public void visitCatch(JCCatch arg0) {
        print(arg0, "CATCH");
        super.visitCatch(arg0);
    }

    @Override
    public void visitClassDef(JCClassDecl ct) {
        boolean oldDoPrint = doPrint;
        // A class can be annotated with @PrintAST, so check modifiers here.
        if (shouldPrintASTFor(ct.mods) && !doPrint) {
            doPrint = true;
            indent = 0;
        }
        print(ct, "CLASS");
        super.visitClassDef(ct);
        doPrint = oldDoPrint;
    }

    @Override
    public void visitConditional(JCConditional arg0) {
        print(arg0, "CONDITIONAL");
        super.visitConditional(arg0);
    }

    @Override
    public void visitContinue(JCContinue arg0) {
        print(arg0, "CONTINUE, label = %s", arg0.label);
        super.visitContinue(arg0);
    }

    @Override
    public void visitDoLoop(JCDoWhileLoop arg0) {
        print(arg0, "DO");
        super.visitDoLoop(arg0);
    }

    @Override
    public void visitExec(JCExpressionStatement arg0) {
        print(arg0, "EXPRESSIONSTATEMENT");
        super.visitExec(arg0);
    }

    @Override
    public void visitForLoop(JCForLoop arg0) {
        print(arg0, "FOR");
        super.visitForLoop(arg0);
    }

    @Override
    public void visitForeachLoop(JCEnhancedForLoop arg0) {
        print(arg0, "FOREACH");
        super.visitForeachLoop(arg0);
    }

    @Override
    public void visitIdent(JCIdent arg0) {
        print(arg0, "IDENT, name = %s", arg0.getName());
        super.visitIdent(arg0);
    }

    @Override
    public void visitIf(JCIf arg0) {
        print(arg0, "IF");
        super.visitIf(arg0);
    }

    @Override
    public void visitIndexed(JCArrayAccess arg0) {
        print(arg0, "ARRAYACCESS");
        super.visitIndexed(arg0);
    }

    @Override
    public void visitLabelled(JCLabeledStatement arg0) {
        boolean oldDoPrint = doPrint;
        // We don't need an annotation to enable AST printing. Why not look
        // for a statement label?
        if ("PrintAST".equals(arg0.getLabel().toString()) && !doPrint) {
            doPrint = true;
            indent = 0;
        }
        print(arg0, "LABELLED, label = %s", arg0.getLabel());
        super.visitLabelled(arg0);
        doPrint = oldDoPrint;
    }

    @Override
    public void visitLetExpr(LetExpr arg0) {
        print(arg0, "LET");
        super.visitLetExpr(arg0);
    }

    @Override
    public void visitLiteral(JCLiteral arg0) {
        print(arg0, "LITERAL, kind = %s, value = %s", arg0.getKind(), arg0.getValue());
        super.visitLiteral(arg0);
    }

    @Override
    public void visitMethodDef(JCMethodDecl arg0) {
        boolean oldDoPrint = doPrint;
        // A method can be annotated with @PrintAST, so check modifiers here.
        if (shouldPrintASTFor(arg0.getModifiers()) && !doPrint) {
            doPrint = true;
            indent = 0;
        }
        print(arg0, "METHOD, name = %s", arg0.getName());
        super.visitMethodDef(arg0);
        doPrint = oldDoPrint;
    }

    @Override
    public void visitModifiers(JCModifiers arg0) {
        print(arg0, "MODIFIERS, flags = [%s]", Flags.toString(arg0.flags));
        super.visitModifiers(arg0);
    }

    @Override
    public void visitNewArray(JCNewArray arg0) {
        print(arg0, "NEWARRAY");
        super.visitNewArray(arg0);
    }

    @Override
    public void visitNewClass(JCNewClass arg0) {
    	//TODO: constructorType, Java 7
        print(arg0, "NEWCLASS, constructor = %s, varargs element = %s",
                describe(arg0.constructor), 
                describe(arg0.varargsElement));
        super.visitNewClass(arg0);
    }

    @Override
    public void visitParens(JCParens arg0) {
        print(arg0, "PARENS");
        super.visitParens(arg0);
    }

    @Override
    public void visitReturn(JCReturn arg0) {
        print(arg0, "RETURN");
        super.visitReturn(arg0);
    }

    @Override
    public void visitSelect(JCFieldAccess arg0) {
        print(arg0, "SELECT, name = %s", arg0.getIdentifier());
        super.visitSelect(arg0);
    }

    @Override
    public void visitSkip(JCSkip arg0) {
        print(arg0, "SKIP");
        super.visitSkip(arg0);
    }

    @Override
    public void visitSwitch(JCSwitch arg0) {
        print(arg0, "SWITCH");
        super.visitSwitch(arg0);
    }

    @Override
    public void visitSynchronized(JCSynchronized arg0) {
        print(arg0, "SYNCHRONIZED");
        super.visitSynchronized(arg0);
    }

    @Override
    public void visitThrow(JCThrow arg0) {
        print(arg0, "THROW");
        super.visitThrow(arg0);
    }

    @Override
    public void visitTry(JCTry arg0) {
        print(arg0, "TRY");
        super.visitTry(arg0);
    }

    @Override
    public void visitTypeApply(JCTypeApply arg0) {
        print(arg0, "TYPEAPPLY");
        super.visitTypeApply(arg0);
    }

    @Override
    public void visitTypeArray(JCArrayTypeTree arg0) {
        print(arg0, "TYPEARRAY");
        super.visitTypeArray(arg0);
    }

    @Override
    public void visitTypeCast(JCTypeCast arg0) {
        print(arg0, "TYPECAST");
        super.visitTypeCast(arg0);
    }

    @Override
    public void visitTypeIdent(JCPrimitiveTypeTree arg0) {
        print(arg0, "PRIMITIVETYPE, kind = %s", arg0.getPrimitiveTypeKind().toString());
        super.visitTypeIdent(arg0);
    }

    @Override
    public void visitTypeParameter(JCTypeParameter arg0) {
        print(arg0, "TYPEPARAMETER, name = %s", arg0.getName());
        super.visitTypeParameter(arg0);
    }

    @Override
    public void visitTypeTest(JCInstanceOf arg0) {
        print(arg0, "INSTANCEOF");
        super.visitTypeTest(arg0);
    }

    @Override
    public void visitUnary(JCUnary arg0) {
        print(arg0, "UNARY, tag = %s, operator = %s", JavacUtils.INSTANCE.describeTreeTag(arg0),
        		describe(arg0.getOperator()));
        super.visitUnary(arg0);
    }

    @Override
    public void visitVarDef(JCVariableDecl arg0) {
        boolean oldDoPrint = doPrint;
        // A variable declaration can be annotated with @PrintAST, so check modifiers here.
        if (shouldPrintASTFor(arg0.mods) && !doPrint) {
            doPrint = true;
            indent = 0;
        }
        print(arg0, "VARDEF, name = %s", arg0.getName());
        super.visitVarDef(arg0);
        doPrint = oldDoPrint;
    }

    @Override
    public void visitWhileLoop(JCWhileLoop arg0) {
        print(arg0, "WHILE");
        super.visitWhileLoop(arg0);
    }

    @Override
    public void visitWildcard(JCWildcard arg0) {
        print(arg0, "WILDCARD");
        super.visitWildcard(arg0);
    }

    public void scan(JCTree tree) {
        indent++;
        super.scan(tree);
        indent--;
    }

    protected void print(JCTree tree, String fmt, Object... args) {
        if (doPrint) {
            String msg = String.format(fmt, args);
            if (tree.type != null) {
            	msg += ", type = " + describe(tree.type);
            }
            messager.printMessage(Kind.NOTE, indentation() + msg);
        }
    }

    private String indentation() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }
        
    private boolean shouldPrintASTFor(JCModifiers mods) {
    	return JavacUtils.INSTANCE.hasAnnotation(mods, PrintAST.class);
    }
}
