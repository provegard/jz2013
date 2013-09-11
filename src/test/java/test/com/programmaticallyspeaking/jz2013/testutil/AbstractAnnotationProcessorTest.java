package test.com.programmaticallyspeaking.jz2013.testutil;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * Based on code from: https://code.google.com/p/aphillips/
 * Licensed under GNU GPL v3.
 */
public abstract class AbstractAnnotationProcessorTest {
	protected final String SOURCE_FILE_SUFFIX = ".java";
	private final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

	/**
	 * @return the processor instances that should be tested
	 */
	protected abstract Collection<? extends Processor> getProcessors();

	/**
	 * Attempts to compile the given compilation units using the Java Compiler
	 * API.
	 * <p>
	 * The compilation units and all their dependencies are expected to be on
	 * the classpath.
	 * 
	 * @param compilationUnitPaths
	 *            the paths of the source files to compile, as would be expected
	 *            by {@link ClassLoader#getResource(String)}
	 * @return the {@link Diagnostic diagnostics} returned by the compilation,
	 *         as demonstrated in the documentation for {@link JavaCompiler}
	 */
	protected List<Diagnostic<? extends JavaFileObject>> compileTestCase(List<String> options,
			String... compilationUnitPaths) {

		if (COMPILER == null) {
			throw new IllegalStateException("No Java compiler available.");
		}

		final Collection<File> compilationUnits = toCompilationUnits(compilationUnitPaths);
		final DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
		final StandardJavaFileManager fileManager = COMPILER
				.getStandardFileManager(diagnosticCollector, null,
						Charset.forName("UTF-8"));
		try {
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT,
					asList(getClassOutputPath()));
		} catch (IOException exc) {
			throw new RuntimeException(exc);
		}

		CompilationTask task = COMPILER.getTask(new PrintWriter(System.out),
				fileManager, diagnosticCollector, options, null,
				fileManager.getJavaFileObjectsFromFiles(compilationUnits));
		task.setProcessors(getProcessors());
		task.call();

		try {
			fileManager.close();
		} catch (IOException exception) {
		}

		return diagnosticCollector.getDiagnostics();
	}

	/**
	 * @return where to put compiled classes
	 */
	protected abstract File getClassOutputPath();

	protected Collection<File> toCompilationUnits(
			String... compilationUnitPaths) {
		assert (compilationUnitPaths != null);

		Collection<File> compilationUnits;

		try {
			compilationUnits = findClasspathFiles(compilationUnitPaths);
		} catch (IOException exception) {
			throw new IllegalArgumentException(
					"Unable to resolve compilation units "
							+ Arrays.toString(compilationUnitPaths)
							+ " due to: " + exception.getMessage(), exception);
		}
		return compilationUnits;
	}

	private Collection<File> findClasspathFiles(String[] filenames)
			throws IOException {
		final Collection<File> classpathFiles = new ArrayList<File>(
				filenames.length);
		for (final String filename : filenames) {
			final File file = getFile(filename);
			assert file.exists() && file.canRead() && file.isFile() : file
					.getAbsolutePath();
			classpathFiles.add(file);
		}

		return classpathFiles;
	}

	protected File getFile(String filename) throws IOException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			return new File(cl.getResource(filename).toURI());
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}
	
	protected void assertCompilationPrinted(String message,
			List<Diagnostic<? extends JavaFileObject>> diagnostics) {
		assertCompilationPrinted(Kind.NOTE, message, diagnostics);
	}

	protected void assertCompilationPrinted(Kind expectedKind, String message,
			List<Diagnostic<? extends JavaFileObject>> diagnostics) {
		assert diagnostics != null;
		boolean expectedDiagnosticFound = false;

		StringBuilder sb = new StringBuilder();
		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {

			sb.append(
					String.format("%s (%d): %s", diagnostic.getKind(),
							diagnostic.getLineNumber(),
							diagnostic.getMessage(null))).append("\n");

			String actualMessage = diagnostic.getMessage(null);
			if (diagnostic.getKind() == expectedKind && actualMessage.contains(message)) {
				expectedDiagnosticFound = true;
				break;
			}
		}

		assertTrue(expectedDiagnosticFound, "Expected a result with message "
				+ message + " of kind " + expectedKind + ", but got:\n\n" + sb.toString());
	}
}
