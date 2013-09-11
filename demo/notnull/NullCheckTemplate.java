package notnull;

import com.programmaticallyspeaking.jz2013.*;

public class NullCheckTemplate {

	private static void callMe(String x) {
		System.out.println("In uppercase: " + x.toUpperCase());
	}
	
	private static void template(String x) {
		PrintAST: if (x == null)
			throw new IllegalArgumentException("Parameter 'x' is null.");
	}

	public static void main(String[] args) {
		callMe(null);
		template(null);
	}

}