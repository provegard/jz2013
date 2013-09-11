package astex;

import com.programmaticallyspeaking.jz2013.*;
import java.util.*;
import java.util.concurrent.Callable;

@PrintAST
public class TypeInvestigation implements Callable<String> {

	private static Random rand = new Random();

	public static void main(String[] args) throws Exception {
		new TypeInvestigation().call();
	}

	public String call() throws Exception {
		return toString();
	}
}