package test.com.programmaticallyspeaking.jz2013;

import static org.fest.assertions.Assertions.assertThat;
import static org.testng.Assert.fail;

import java.util.Collections;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.testng.annotations.Test;

import test.com.programmaticallyspeaking.jz2013.testutil.MainProcessorTest;

import com.programmaticallyspeaking.jz2013.NotNull;

public class NotNullTest extends MainProcessorTest {

	private String repeatIt(@NotNull String x) {
		return x + x;
	}

	@Test
	public void shouldCorrectlyAddNullCheckForInnerMethod() {
		try {
			new ParamRunnable() {
				public void run(@NotNull Object p) {
				}
			}.run(null);
			fail("IllegalArgumentException was expected.");
		} catch (IllegalArgumentException e) {
			StackTraceElement stackTraceElement = e.getStackTrace()[0];
			assertThat(stackTraceElement.getLineNumber()).isEqualTo(29);
		}
	}

	@Test
	public void shouldCorrectlyAddNullCheckWhenThereAreNestedBlocks() {
		try {
			new ParamRunnable() {
				public void run(@NotNull Object p) {
					if (false) {
					}
				}
			}.run(null);
			fail("IllegalArgumentException was expected.");
		} catch (IllegalArgumentException e) {
			StackTraceElement stackTraceElement = e.getStackTrace()[0];
			assertThat(stackTraceElement.getLineNumber()).isEqualTo(43);
		}
	}

	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void shouldDoArgumentNullCheck() {
		repeatIt(null);
	}

	@Test
	public void shouldSucceedWhenArgIsNotNull() {
		assertThat(repeatIt("hello")).isEqualTo("hellohello");
	}

	@Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*'x' is null.*")
	public void shouldExplainNullCheckFailure() {
		repeatIt(null);
	}

	@Test
	public void shouldHaveProperSourcePositionsInStackTrace() {
		try {
			repeatIt(null);
			fail("IllegalArgumentException was expected.");
		} catch (IllegalArgumentException e) {
			StackTraceElement stackTraceElement = e.getStackTrace()[0];
			assertThat(stackTraceElement.getLineNumber()).isEqualTo(21);
		}
	}
	
	@Test
	public void shouldEmitNoteWhenGeneratingNullCheckCode() {
		List<Diagnostic<? extends JavaFileObject>> output = 
				compileTestCase(OPTS, "test/com/programmaticallyspeaking/jz2013/NotNullOnParameter.java");
		assertCompilationPrinted("Generated null check for parameter 'p1' of method 'someMethod'.", output);
	}

	@Test
	public void shouldEmitErrorWithNotNullOnPrimitiveTypeParam() {
		List<Diagnostic<? extends JavaFileObject>> output = 
				compileTestCase(OPTS, "test/com/programmaticallyspeaking/jz2013/NotNullOnPrimitiveType.java");

		assertCompilationPrinted(Kind.ERROR, "Cannot use @NotNull for parameter 'p1' of method 'someMethod' because it has a primitive type.", 
				output);
	}

	public static interface ParamRunnable {
		void run(Object p);
	}

	private static final List<String> OPTS = Collections.emptyList();
}
