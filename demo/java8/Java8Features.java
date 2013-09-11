package java8;

import com.programmaticallyspeaking.jz2013.*;
import java.util.*;

//@PrintAST
public class Java8Features {

	public static int greater(int a, int b) {
		return a > b ? a : b;
	}

	@PrintAST
	public static void main(String[] args) throws Exception {
		printOne(6, 5, Java8Features::greater);
		printOne(1, 2, (a, b) -> a * b);
	}

	private static void printOne(int a, int b, Selector sel) {
		System.out.println(sel.select(a, b));
	}
	
	private static interface Selector {
		int select(int a, int b);
	}
}