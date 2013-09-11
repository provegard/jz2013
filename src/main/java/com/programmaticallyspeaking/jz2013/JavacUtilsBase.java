package com.programmaticallyspeaking.jz2013;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.util.List;

public class JavacUtilsBase {
	public boolean hasAnnotation(JCModifiers mods, Class<?> annotationClass) {
		for (List<JCAnnotation> l = mods.annotations; l.nonEmpty(); l = l.tail) {
			if (isAnnotation(l.head, annotationClass))
				return true;
		}
		return false;
	}

	private boolean isAnnotation(JCAnnotation ann, Class<?> annotationClass) {
		if (ann.type == null || ann.type.tsym == null) {
			// Full type info not available, but see if we can match
			// on the unqualified name.
			return annotationClass.getSimpleName().equals(ann.annotationType.toString());
		}
		return annotationClass.getName().equals(
				ann.type.tsym.getQualifiedName().toString());
	}

	public int getIntegerTag(Class<?> clazz, String name) {
		try {
			return (Integer) clazz.getField(name).get(null);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(String.format(
					"Non-public field: %s.%s", clazz.getName(), name));
		} catch (NoSuchFieldException e) {
			throw new IllegalArgumentException(String.format(
					"Missing field: %s.%s", clazz.getName(), name));
		}
	}
}
