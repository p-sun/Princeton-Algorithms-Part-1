import java.util.Arrays;


public class Fast {

	public static void main(String[] args) {
		// Read input and place into points array. Same as Brute.java ---------
		 // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        StdDraw.setPenRadius(0.01);  // make the points a bit larger

        // read in the input with arg[0]    or    with String
        //String filename = args[0];
        String filename = "datasets/input200.txt";
        
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
		/*
    	for (Point pt: points){
    		StdOut.println("points " + pt);
    	}
    	*/
		
        // ------------------------------------
        
		Point[] aux = new Point[N];
		Point[][] lastPoints = new Point[N*N][2]; 
			// Tracks all the "last points" in lines drawn. 
			// lastPoints[0] tracks the final pt. [1] tracks the second-to-last point.
				// b/c lines may share the last pt, but are not the same line
				// If you pick 50 items in a 10x10 grid, you can get a lot of lines!
				// Hence N*N
		
		int lastPointsi = 0; // The index of the next index to add another point
		
        for (int p = 0; p < N-3; p++) { // p = 0 -> 4th last
        	aux = points.clone(); 
        		//Clone everything so that the p index stay the same as the original
        		// Even though you take a lot of time to clone everything,
        		// it's still faster to reuse one array than to make an new aux
        		// for each new p.
        	Arrays.sort(aux, p+1, N, aux[p].SLOPE_ORDER); 
        		// sort lo (p+1) to hi (last)
 
        	/*
    		StdOut.println("p " + p + " aux[p] " + aux[p]);
        	for (Point pt: aux){
        		StdOut.println("point " + pt);
        	}
        	*/
        	
        	int count = 1; // Next turn, compare slopes of p & q and p & (q+count) 
        	int q = p+1; //
        	while (q < N) {
        		while (q+count < N && aux[p].SLOPE_ORDER.compare(aux[q], aux[q+count]) == 0)  {
        			/*
           			StdOut.println("q: " + q);
        			StdOut.println("count: " + count);
        			StdOut.println("SLOPE_ORDER: " + aux[p].SLOPE_ORDER.compare(aux[q], aux[q+count]));
        			*/
        			count++;
        		}
        		
           		// draw line from q to q+count-1 if 4 or more points in a row
        		if (count >= 3) {
        			
        			// Check if the last point has already been included in a line
        			boolean lineIsDrawn = false;
        			for (int k = 0; k < lastPointsi; k++) {
        				//StdOut.println("[0] " + lastPoints[k][0] + " q+count-1 " + aux[q+count-1]);
        				//StdOut.println("[1] " + lastPoints[k][1] + " q+count-2 " + aux[q+count-2]);

        				if (lastPoints[k][0] == aux[q+count-1] && lastPoints[k][1] == aux[q+count-2]) {
        					lineIsDrawn = true;
        					break;
        				}
        			}
        			
        			if (!lineIsDrawn) {
            			// Print & Draw the line
            			StdOut.print(aux[p]);
            			for (int j = 0; j < count; j++) {
            				StdOut.print( " -> " + aux[q+j]);
            			}
            			StdOut.println("");
            			aux[p].drawTo(aux[q+count-1]);
            			
            			// Add the last point to an array
            			//StdOut.println("last points: " + aux[q+count-1]);
            			//StdOut.println("last pointsi: " + lastPointsi);
            			lastPoints [lastPointsi][0] = aux[q+count-1];
            			lastPoints [lastPointsi++][1] = aux[q+count-2];

        			}

        		}
        		
        		q = q + count;
        		count = 1;
        	}
        	
        }
        
        /*
		StdOut.println("last points " + lastPointsi);
    	for (Point pt: lastPoints){
    		StdOut.println("point " + pt);
    	}
        */
        
        // display to screen all at once
        StdDraw.show(0);

        // reset the pen radius
        StdDraw.setPenRadius();
		
		

	}

}
