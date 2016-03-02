/*
 * // http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 * Brute-force implementation to return all the points within a rectangle and closest neighbor to a point.
 * Write a mutable data type PointSET.java that represents a set of points in the unit square. 
 * Implement the following API by using a red-black BST (using either SET from algs4.jar or java.util.TreeSet).
 * Performance requirements:
 *		insert() and contains() in time proportional to   log N
 *		nearest() and range() in time proportional to   N
 */

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> set;

	public PointSET() { // construct an empty set of points 
		set = new TreeSet<Point2D>();
	}
	
	public boolean isEmpty() { // is the set empty? 
		return set.isEmpty();
	}
	
	public int size() { // number of points in the set 
		return set.size();
	}
	
	public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
		if (p == null) throw new java.lang.NullPointerException("argument point is null.");
		set.add(p);
	}
	
	public boolean contains(Point2D p) { // does the set contain point p? 
		if (p == null) throw new java.lang.NullPointerException("argument point is null.");
		return set.contains(p);
	}
	
	public void draw() { // draw all points to standard draw 
		for (Point2D p : set) {
			p.draw();
		}
	}
	
	public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle 
		if (rect == null) throw new java.lang.NullPointerException("argument rectangle is null.");

		Stack<Point2D> pts = new Stack<Point2D>();
		
		for (Point2D p : set) {
			if (rect.xmin() <= p.x() && rect.xmax() >= p.x() && rect.ymin() <= p.y() && rect.ymax() >= p.y()) {
				pts.push(p);
			}
		}

		return pts;
	}
	
	//return nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) { 
		if (p == null) throw new java.lang.NullPointerException("argument point is null.");
		
		double minDis = Double.MAX_VALUE; //min distance encountered so far to p, squared
			// Squared distances. Whenever you need to compare two Euclidean distances, it is often 
			// more efficient to compare the squares of the two distances to avoid the expensive 
			// operation of taking square roots. 
		Point2D closest = null; //closest neighbor
		
		for (Point2D q : set){
			if (p.distanceSquaredTo(q) < minDis) {
				minDis = p.distanceSquaredTo(q);
				closest = q;
			}
		}
		
		return closest;
	}
	
	
	
	public static void main(String[] args) {
	   PointSET s = new PointSET();
	   StdOut.println("isEmpty: " + s.isEmpty());

	   s.insert(new Point2D(0.3, 0.7));
	   s.insert(new Point2D(0.1, 0.1));
	   s.insert(new Point2D(0.4, 0.8));
	   
	   StdOut.println("size: " + s.size());

	   StdDraw.setPenRadius(0.01);
	   s.draw(); // Default scale for drawing is 0-1
	   StdDraw.show();
	   
	   Iterable<Point2D> r = s.range(new RectHV(2500, 2000, 16000, 16000));
	   for (Point2D p : r) {
		   StdOut.println(p.toString());
	   }
	}
   
}