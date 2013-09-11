package test.com.programmaticallyspeaking.jz2013;

import org.testng.annotations.Test;

import com.programmaticallyspeaking.jz2013.PrintAST;

public class PrintASTOnMethod {

	@PrintAST
    public void someMethod() {
        String x = System.getenv("user.home");
        if (x != null)
        	System.out.println("User home: " + x);
    }
}
