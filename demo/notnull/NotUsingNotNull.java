package notnull;

public class NotUsingNotNull {

	private static void callMe(String x) {
		System.out.println("In uppercase: " + x.toUpperCase());
	}
	
	public static void main(String[] args) {
		callMe(null);
	}

}