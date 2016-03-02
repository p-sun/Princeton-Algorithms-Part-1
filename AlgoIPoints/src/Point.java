/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {;
    
    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // -------------- My Code from here on -------------------------------------------------    
    
    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    // The compareTo() method should compare points by their y-coordinates, breaking ties by their x-coordinates. 
    // Formally, the invoking point (x0, y0) is less than the argument point (x1, y1) if and only if 
    // either y0 < y1 or if y0 = y1 and x0 < x1.
    
    public int compareTo(Point that) {
    	if (this.y < that.y) return -1;
    	if (this.y > that.y) return 1;
    	if (this.y == that.y) {
    		if (this.x < that.x) return -1;
    		else if(this.x > that.x) return 1;
    	}
    	return 0;
    }
    
    // slope between this point and that point.
    // Horizontal line -> slope positive 0. 
    	// 3-2 is positive 0. 2-3 is negative 0. positive 0 > negative 0
    // Same point -> -INFINITY
    // Vertical line -> +INFINITY
    public double slopeTo(Point that) {
    	if (this.x == that.x) {
    		if (this.y == that.y) return Double.NEGATIVE_INFINITY;
    		return Double.POSITIVE_INFINITY;
    	}  	
    	if (this.y == that.y) return 0.0; 
    	
    	return (double)(that.y - this.y) / (double)(that.x - this.x);
    }

    // The SLOPE_ORDER comparator should compare points by the slopes they make with the invoking point (x0, y0).
    // Formally, the point (x1, y1) is less than the point (x2, y2) if and only 
    // if the slope (y1 − y0) / (x1 − x0) is less than the slope (y2 − y0) / (x2 − x0). 
    // Treat horizontal, vertical, and degenerate line segments as in the slopeTo() method.
    	// +INF - +INF  ==  NaN  | +INF +  + INF  ==  +INF | 99999999 + +INF == +INF
    
    public final Comparator<Point> SLOPE_ORDER = new Comparator<Point>() {
    	public int compare (Point q, Point r) {
    		double slopeq = Point.this.slopeTo(q);
    		double sloper = Point.this.slopeTo(r);
    		if (slopeq < sloper) return -1;
    		else if (slopeq == sloper) return 0; 
    			// Must compare like this b/c both may be -INF or + INF
    		else return 1;
    	}
    	
    };
    
    
    // For learning purposes. Can also do this to implement SLOPE_ORDER,
    // but this is more awkward.
    /*
    public final Comparator<Point> SLOPE_ORDER = new BySlopeOrder();
    
    private class BySlopeOrder implements Comparator<Point> {
    	public int compare (Point p, Point q) {
    		StdOut.println(Point.this.x);
    		return -1;
    	}
    	
    }
    */
   
    // unit test
    public static void main(String[] args) {    	
    	Point q = new Point(10000, 0);
    	Point r = new Point(6000, 7000);
    	Point s = new Point(1000, 15000); 
    	
    	Point[] points = new Point[3]; 
    	points[0] = new Point(10000, 10000);
    	points[1] = new Point(12000, 12000);
    	points[2] = new Point(14000, 14000);

    	
    	
    	System.out.println("compareTo: " + q.compareTo(r));
    	
       	System.out.println("slopeTo: " + q.slopeTo(r));
           	
       	System.out.println("SLOPE_ORDER: " + q.SLOPE_ORDER.compare(q, r));
       	
       	System.out.println("infinity + infinity : " + (66767887 + Double.POSITIVE_INFINITY));
    	 
    	
    	StdDraw.setXscale(0, 32768);
    	StdDraw.setYscale(0, 32768);
    	StdDraw.setPenRadius(0.01);
    	
    	for (Point p: points) {
    		p.draw();
    	}
    	
    	
       	s.draw();
       	q.drawTo(r);

       	StdDraw.show();

    }
}