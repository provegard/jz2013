package com.programmaticallyspeaking.jz2013;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;

public class JavacUtils extends JavacUtilsBase {
	public static final JavacUtils INSTANCE = new JavacUtils();

	public JCLiteral createNullLiteral(TreeMaker treeMaker) {
		// Java 8 introduced the TypeTag enum to replace the integer constants
		// in the TypeTags class.
		return treeMaker.Literal(TypeTag.BOT, null);
	}

	public JCBinary createEqualsComparison(TreeMaker treeMaker, JCExpression lhs,
			JCExpression rhs) {
		// In Java 8, the JCTree.Tag enum replaces the integer constants previously
		// find in the JCTree class.
		return treeMaker.Binary(JCTree.Tag.EQ, lhs, rhs);
	}

	public String describeTypeTag(Type type) {
		return typeTagToString(type.getTag());
	}

	public String describeTreeTag(JCTree tree) {
		JCTree.Tag tag = tree.getTag();
    	return tag.toString() + "/" + tag.ordinal();
	}

    private String typeTagToString(TypeTag tag) {
    	return tag.toString() + "/" + tag.ordinal();
    }
}
