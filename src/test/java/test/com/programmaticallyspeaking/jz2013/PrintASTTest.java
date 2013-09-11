package test.com.programmaticallyspeaking.jz2013;

import java.util.Collections;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.testng.annotations.Test;

import test.com.programmaticallyspeaking.jz2013.testutil.MainProcessorTest;


public class PrintASTTest extends MainProcessorTest {
	
	private static final List<String> OPTS = Collections.singletonList("-Adoea=true");
	
	@Test
	public void shouldFindMethod() {
		List<Diagnostic<? extends JavaFileObject>> output = 
				compileTestCase(OPTS, "test/com/programmaticallyspeaking/jz2013/PrintASTOnMethod.java");
		assertCompilationPrinted("name = someMethod", output);
	}

	@Test
	public void shouldFindVariable() {
		List<Diagnostic<? extends JavaFileObject>> output = 
				compileTestCase(OPTS, "test/com/programmaticallyspeaking/jz2013/PrintASTInsideMethod.java");
		assertCompilationPrinted("name = x", output);
	}

	@Test
	public void shouldPrintTypeInfo() {
		List<Diagnostic<? extends JavaFileObject>> output = 
				compileTestCase(OPTS, "test/com/programmaticallyspeaking/jz2013/PrintASTInsideMethod.java");
		assertCompilationPrinted("type = java.lang.System", output);
	}

	@Test
	public void shouldBeTriggeredByPrintASTLabel() {
		List<Diagnostic<? extends JavaFileObject>> output = 
				compileTestCase(OPTS, "test/com/programmaticallyspeaking/jz2013/PrintASTAsLabel.java");
		assertCompilationPrinted("name = println", output);
	}
}
