package com.programmaticallyspeaking.jz2013;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.TreeMaker;

public class JavacUtils67 extends JavacUtilsBase {
	
	public JCLiteral createNullLiteral(TreeMaker treeMaker) {
		// Instead of accessing TypeTags.BOT directly, which may cause problems
		// across Java versions (possibly even within the same major version)
		// due to compile-time constant inlining, we can access the constant
		// using reflection.
		int bot = getIntegerTag(TypeTags.class, "BOT");
		return treeMaker.Literal(bot, null);
	}

	public String describeTypeTag(Type type) {
		return typeTagToString(type.tag);
	}

	public JCBinary createEqualsComparison(TreeMaker treeMaker, JCExpression lhs,
			JCExpression rhs) {
		// Accessing JCTree.EQ directly here means that 1) it is necessary
		// to build different JARs that target different Java versions, and 2)
		// that the EQ constant must not change within the major Java version.
		return treeMaker.Binary(JCTree.EQ, lhs, rhs);
	}
	
    private static final String[] TYPETAGS = {"BYTE", "CHAR", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BOOLEAN", "VOID"};
    private String typeTagToString(int tag) {
        int val = tag - 1;
        return val >= 0 && val < TYPETAGS.length ? TYPETAGS[val] : Integer.toString(tag);
    }
}
