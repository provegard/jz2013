package com.programmaticallyspeaking.jz2013;

import com.sun.tools.javac.tree.JCTree;

public class JavacUtils extends JavacUtils67 {
	public static final JavacUtils INSTANCE = new JavacUtils();

	public String describeTreeTag(JCTree tree) {
		return Integer.toString(tree.getTag());
	}
}
