package test.com.programmaticallyspeaking.jz2013;

import org.testng.annotations.Test;

import com.programmaticallyspeaking.jz2013.PrintAST;

public class PrintASTInsideMethod {

    public void someMethod() {
    	@PrintAST
        String x = System.getenv("user.home");
        if (x != null)
        	System.out.println("User home: " + x);
    }
}
