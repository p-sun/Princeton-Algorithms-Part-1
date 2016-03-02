import java.util.Arrays;

// Write a program Brute.java that examines 4 points at a time 
// and checks whether they all lie on the same line segment,
// printing out any such line segments to standard output and drawing them using standard drawing. 

// To check whether the 4 points p, q, r, and s are collinear, 
// check whether the slopes between p and q, between p and r, and between p and s are all equal.

// The order of growth of the running time of your program should be N4 
// in the worst case and it should use space proportional to N.


public class Brute {
	
	public static void main(String[] args) {
		
		 // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        StdDraw.setPenRadius(0.01);  // make the points a bit larger

        // read in the input with arg[0]    or    with String
        String filename = args[0];
        //String filename = "datasets/input8.txt";
        
        In in = new In(filename);
        int N = in.readInt();
        Point[] points = new Point[N];
        
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            points[i] = p;
            p.draw();
        }

		Arrays.sort(points);
			// Sort the points, so that when you systematically pick 4 points,
			// point p < q < r < s
			// So then you only have to draw a line from point p to s
        
        for (int p = 0; p < N; p++) {
        	for (int q  = p+1; q < N; q++) {
        		for (int r = q+1; r < N; r++) {
        			
    				double ptoq = points[p].slopeTo(points[q]);
    				double ptor = points[p].slopeTo(points[r]);
    			
    				if (ptoq == ptor) {

            			for (int s = r+1; s < N; s++){
            				double ptos = points[p].slopeTo(points[s]);
            				if (ptoq == ptos){
            					
                				StdOut.println(points[p] + " -> " + points[q] + " -> " + points[r] 
                						+ " -> " + points[s]);
                				points[p].drawTo(points[s]);            				}
            			}    				
    				}        			
        		}
        	}
        }
        
        
        // display to screen all at once
        StdDraw.show(0);

        // reset the pen radius
        StdDraw.setPenRadius();
		
		
	}

}
