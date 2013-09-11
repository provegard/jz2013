package astex;

import com.programmaticallyspeaking.jz2013.*;
import java.util.*;

public class ForeachLoop {
	@PrintAST
	public static void main(String[] args) {
		List<Integer> numbers = Arrays.asList(1, 2, 3);
		for (int i : numbers) {
		}
	}

}