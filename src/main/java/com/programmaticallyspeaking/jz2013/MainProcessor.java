package com.programmaticallyspeaking.jz2013;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

/**
 * This is our main annotation processor. It can be specified using the
 * -processor command-line option, or in a
 * META-INF/services/javax.annotation.processing.Processor resource. We use
 * {@link SupportedAnnotationTypes} to specify that we want all annotation. If
 * we specified exact types, our processor woudln't be invoked for annotations
 * inside methods, since JSR-269 stops at the method level.
 */
@SupportedAnnotationTypes("*")
@SupportedOptions("doea")
public class MainProcessor extends AbstractProcessor {

	private JavacProcessingEnvironment env;
	private Messager messager;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		messager = processingEnv.getMessager();
		
		// We tie ourselves to javac already here!
		env = (JavacProcessingEnvironment) processingEnv;

		super.init(processingEnv);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {

		if (roundEnv.processingOver())
			return false;

		messager.printMessage(Kind.NOTE,
				"MainProcessor is diving into AST land.");

		Set<? extends Element> rootElements = roundEnv.getRootElements();
		Map<String, String> options = env.getOptions();

		// Context is very useful to pass around, because all the interesting
		// helper classes can be constructed from it!
		Context context = env.getContext();

		// Trees is NOT unsupported API
		Trees trees = Trees.instance(env);
		
		EarlyAttribution earlyAttribution = null;
		if ("true".equals(options.get("doea"))) {
			earlyAttribution = new EarlyAttribution(context);
		}

		for (Element e : rootElements) {
			if (e.getKind() != ElementKind.CLASS)
				continue;

			if (earlyAttribution != null) {
				// Do early attribution so that we get full type information!
				earlyAttribution.attributeClass(e);
			}

			// JCTree is unsupported API!
			JCTree t = (JCTree) trees.getPath(e).getCompilationUnit();

			new ASTPrinter(messager).scan(t);
			new NullCheckGenerator(context, messager).translate(t);
		}
		
		// Don't claim any annotations since we're a universal processor.
		return false;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		// We support all Java versions...
		return SourceVersion.latestSupported();
	}
}
