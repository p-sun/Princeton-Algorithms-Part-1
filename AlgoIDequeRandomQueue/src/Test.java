
public class Test {
	public static void main(String[] args) {
		
		int h = 4;
		int N = 10;
		
		for (int i = h; i < N; i++)
		{
		   for (int j = i; j >= h; j -= h)
		      System.out.println("i: " + i + " j: " + j);
		}
		
		
	}
}
