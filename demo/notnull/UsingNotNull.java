package notnull;

import com.programmaticallyspeaking.jz2013.*;

public class UsingNotNull {

	private static void callMe(@NotNull String x) {
		System.out.println("In uppercase: " + x.toUpperCase());
	}
	
	public static void main(String[] args) {
		callMe(null);
	}

}