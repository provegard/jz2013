package com.programmaticallyspeaking.jz2013;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.util.Context;

/**
 * This class invokes the attribution phase before it is actually done by the
 * compiler, so that we have type information available when we scan and
 * translate the AST. The backside of this is that if a warning is emitted
 * during the attribution phase, it will be emitted twice. The second time is
 * when the compiler invokes the attribution phase for real!
 */
public class EarlyAttribution {

	private final Attr attr;
	private final JavacTrees trees;

	public EarlyAttribution(Context context) {
		attr = Attr.instance(context);
		trees = JavacTrees.instance(context);
	}

	public void attributeClass(Element classElement) {
		assert classElement.getKind() == ElementKind.CLASS;
		JCClassDecl ct = (JCClassDecl) trees.getTree(classElement);
		if (ct.sym != null) {
			if ((ct.sym.flags_field & Flags.UNATTRIBUTED) != 0) {
				attr.attribClass(ct.pos(), ct.sym);
			}
		}
	}

}
