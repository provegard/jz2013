package astex;

import com.programmaticallyspeaking.jz2013.*;
import java.util.*;

public class TypeUnion {
	@PrintAST
	public static void main(String[] args) {
		try {
			throw new IllegalArgumentException();
		} catch (IllegalArgumentException|NullPointerException ex) {
		}
	}

}