package astex;

import com.programmaticallyspeaking.jz2013.*;
import java.util.*;

public class Conditional {
	@PrintAST
	public static void main(String[] args) {
		String x = new Random().nextInt(10) > 5
			? "More than 5"
			: "Less than 6";
	}

}