package notnull;

import com.programmaticallyspeaking.jz2013.*;

public class NotNullOnInt {

	private static void callMe(@NotNull int x) {
		System.out.println("Value: " + x);
	}
	
	public static void main(String[] args) {
		callMe(0);
	}

}