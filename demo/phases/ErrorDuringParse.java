package phases;

import com.programmaticallyspeaking.jz2013.*;
import java.util.*;

public class ErrorDuringParse {
	public static void main(String[] args) {
		@PrintAST
		System.out.println("Annotation not allowed on statement.");
	}

}