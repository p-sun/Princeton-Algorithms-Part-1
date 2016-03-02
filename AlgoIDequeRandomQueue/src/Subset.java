import java.util.Iterator;

// takes a command-line integer k; 
// reads in a sequence of N strings from standard input using 
//     StdIn.readString();
// and prints out exactly k of them, uniformly at random. 

// Each item from the sequence can be printed out at most once. 
// You may assume that k is between 0 and N, where N is the number of string on
//     standard input.

// To run on terminal, cd to this path. Then,
/*
1) export CLASSPATH=$CLASSPATH:/Users/psun/documents/workspace/AlgoIJar/stdlib.jar
	:/Users/psun/AlgoIJar/algs4-2.jar
2) javac Subset.java       (or even javac -cp stdlib.jar Subset.java)
3) echo AA BB BB BB BB BB CC CC | java Subset 8
See for more examples
http://coursera.cs.princeton.edu/algs4/assignments/queues.html
*/

public class Subset {

	public static void main(String[] args) {
		
		// Read the all the strings before the pipe symbol, |
		RandomizedQueue<String> q = new RandomizedQueue<String>();
		
		while (!StdIn.isEmpty()){
			q.enqueue(StdIn.readString());
			//System.out.println(StdIn.readString());
		}
		
		// Read k, the number of items to print to screen
		int k = Integer.parseInt(args[0]);

		Iterator<String> itr = q.iterator();		
		for (int i= 0; i < k; i++) {
			Object item = itr.next();
			System.out.println(item);
		}		
	}
}
