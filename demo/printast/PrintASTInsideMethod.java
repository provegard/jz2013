package printast;

import com.programmaticallyspeaking.jz2013.*;
import java.util.*;

public class PrintASTInsideMethod {
	public static void main(String[] args) {
		@PrintAST
		String x = System.getenv("java.io.tmpdir");
	}

}