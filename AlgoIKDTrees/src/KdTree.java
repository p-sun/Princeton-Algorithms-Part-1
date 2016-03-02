/* Same API and purpose and PointSET, except faster. Uses red-black BST in a kd-tree instead of brute force
 * 		See Week 5 for kd tree explanation
 * Each node corresponds to an axis-aligned rectangle in the unit square, 
 * which encloses all of the points in its subtree. The root corresponds to the unit square; 
 * the left and right children of the root corresponds to the two rectangles split 
 * by the x-coordinate of the point at the root; and so forth.
 * 
 * Range search. To find all points contained in a given query rectangle, 
 * start at the root and recursively search for points in both subtrees 
 * using the following pruning rule: if the query rectangle does not 
 * intersect the rectangle corresponding to a node, 
 * there is no need to explore that node (or its subtrees).
 * 
 * Nearest neighbor search. To find a closest point to a given query point, start 
 * at the root and recursively search in both subtrees using the following pruning rule:
 * - if the closest point discovered so far is closer than the distance b/w the query point 
 * and the rectangle corresponding to a node, there is no need to explore that node.
 * - when there are two possible subtrees to go down, you always choose the 
 * subtree that is on the same side of the splitting line as the query point
 * as the first subtree to explore
 */

//import java.util.TreeSet;
//import java.util.Comparator;

public class KdTree {
    private Node root;
    private int size;
    
    private static class Node {
    	   private Point2D p;      // the point
    	   private RectHV rect;    // the axis-aligned rectangle corresponding to this node
    	   private Node lb;        // the left/bottom subtree
    	   private Node rt;        // the right/top subtree
    	   
    	   private  Node(Point2D p1, RectHV rect1) {
    		   p = p1;
    		   rect = rect1;
    		   lb = null;
    		   rt = null;
    	   }
    }
    
	public KdTree() { // construct an empty set of points 
		root = null;
	}
	
	public boolean isEmpty() { // is the set empty? 
		return root == null;
	}
	
	public int size() { // number of points in the set 
		return size;
	}
	
	public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
		if (p == null) throw new java.lang.NullPointerException("argument point is null.");
		root = insert(root, p, true);
	}
	
	private Node insert (Node k, Point2D p, boolean byX) { // current Node k is only null when tree is empty
		
		if (k == null) { // If tree is empty(root==null), the root Node's rectangle is (0, 0) to (1, 1)
			size++;			
			return new Node(p, new RectHV(0, 0, 1, 1)); 
		}
		if (k.p.equals(p)) return k; // If the point is already in the tree, stop searching further
		
		boolean goLeft;							// Go down L tree only when
		if (byX) goLeft = p.x() < k.p.x(); // point p is strictly smaller than current node's x-cood
		else goLeft = p.y() < k.p.y();	// splitting byX is either true (even layers) or false (odd layers)
		
		if (goLeft) {
			if (k.lb == null) {
				size++;
				if (byX) k.lb = new Node(p, new RectHV(k.rect.xmin(), k.rect.ymin(), k.p.x(), k.rect.ymax()));
				else k.lb = new Node(p, new RectHV(k.rect.xmin(), k.rect.ymin(), k.rect.xmax(), k.p.y()));
			} else k.lb = insert(k.lb, p, !byX);
		} else { // go to right tree
			if (k.rt == null) {
				size++;
				if (byX) k.rt = new Node(p, new RectHV(k.p.x(), k.rect.ymin(), k.rect.xmax(), k.rect.ymax()));
				else k.rt = new Node(p, new RectHV(k.rect.xmin(), k.p.y(), k.rect.xmax(), k.rect.ymax()));
			} else k.rt = insert(k.rt, p, !byX);
		}
						
		return k;
	}
	
	public boolean contains(Point2D p) { // does the set contain point p? 
		if (p == null) throw new java.lang.NullPointerException("argument point is null.");
		return contains(root, p, true);
	}
	
	private boolean contains(Node k, Point2D p, boolean byX){
		if (k == null) return false; // When tree is empty, or when we reach end of branch

		// Does current Node k contain point p?
		if (k.p.equals(p)) return true;
		
		// Try the L branch or the R branch?
		boolean goLeft;						// Same goLeft logic as insert()
		if (byX) goLeft = p.x() < k.p.x(); 
		else goLeft = p.y() < k.p.y();	
		
		if (goLeft) return contains(k.lb, p, !byX);
		else return contains(k.rt, p, !byX);
		
	}
	
	
	public void draw() { // draw all points to standard draw 
		draw(root, true);
	}
	
	private void draw(Node k, boolean byX) {
		if (k == null) return;
		
		// Draw the point
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(.01);
		k.p.draw(); //TODO I wonder if we did if (k.lb == null) before calling recursion, it'd be faster?
		// Draw the line
		StdDraw.setPenRadius();
		if (byX){
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(k.p.x(), k.rect.ymin(), k.p.x(), k.rect.ymax());
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(k.rect.xmin(), k.p.y(), k.rect.xmax(), k.p.y());
		}
		
		draw(k.lb, !byX);
		draw(k.rt, !byX);
		
	}
	
	public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle 
		if (rect == null) throw new java.lang.NullPointerException("argument rectangle is null.");

		Stack<Point2D> pts = new Stack<Point2D>();
		range(root, rect, pts, true);
		return pts;
	}
	
	private void range(Node k, RectHV rect, Stack<Point2D> pts, boolean byX) {
		if (k == null) return; // if root == null
		
		// push point onto stack if current Node's point inside the rect
		if (rect.contains(k.p)) pts.push(k.p);
		
		// If k's left or right rect intersects with rect, go down that tree
		if (k.lb != null && rect.intersects(k.lb.rect))
			range(k.lb, rect, pts, !byX);
		if (k.rt != null && rect.intersects(k.rt.rect))
			range(k.rt, rect, pts, !byX);
				
	}
	
	// nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) { 
		if (p == null) throw new java.lang.NullPointerException("argument point is null.");
		
		if (root == null) return null; 
		return nearest(root, p, Double.MAX_VALUE, true);
	}
	
	private static Point2D nearest(Node k, Point2D p, double minDis, boolean byX) {
			// minDis - minimum distance encountered so far to p
		if (k == null) return null;

		Point2D minP = null; //The closest point in the trees of both children that is closer than minDis
		
		double kDis = k.p.distanceSquaredTo(p);
		if (kDis < minDis) {
			minP = k.p;
			minDis = kDis;
		}
		
		// Try the branch on the same side as p first
		boolean goLeft;						// Same goLeft logic as insert()
		if (byX) goLeft = p.x() < k.p.x(); 
		else goLeft = p.y() < k.p.y();			
		
		// Try L branch or R branch first depending on which is closer to p
		Point2D p1;
		if (goLeft) p1 = nearest(k.lb, p, minDis, !byX);
		else p1 = nearest(k.rt, p, minDis, !byX);
		if (p1 != null) { // p1 != null if its closest point is closer than minDis
			minP = p1;
			minDis = p1.distanceSquaredTo(p);
		}
		
		Point2D p2 = null; // Only try a path if there may be a point there closer than minDis
 		if (goLeft && k.rt != null && minDis > k.rt.rect.distanceSquaredTo(p)) 
			p2 = nearest(k.rt, p, minDis, !byX);
		else if (!goLeft && k.lb != null && minDis > k.lb.rect.distanceSquaredTo(p))
			p2 = nearest(k.lb, p, minDis, !byX);
		
		if (p2 != null) {
			if (p1 == null) {
				minP = p2;
				minDis = p2.distanceSquaredTo(p);
			} else { //leftp and rightp both != null, minDis is left's distance to p
				double p2Dis = p2.distanceSquaredTo(p);
				if (p2Dis < minDis) {
					minP = p2;
					minDis = p2Dis;
				}
			}
		}
		
		return minP;
	}
	
	public static void main(String[] args) {
		KdTree s = new KdTree();
		StdOut.println("isEmpty before insert: " + s.isEmpty());
		
		s.insert(new Point2D(0.3, 0.7));
		s.insert(new Point2D(0.1, 0.1));
		s.insert(new Point2D(0.4, 0.8));
		   
		StdOut.println("isEmpty after insert: " + s.isEmpty());

		StdOut.println("size: " + s.size());
		
		StdOut.println("root: " + s.root.p);
		StdOut.println("left of root: " + s.root.lb.p);
		StdOut.println("right of root: " + s.root.rt.p);

		s.draw(); // Default scale for drawing is 0-1
		StdDraw.show();
		   
		Iterable<Point2D> r = s.range(new RectHV(2500, 2000, 16000, 16000));
		for (Point2D p : r){
			StdOut.println(p.toString());
		}
	}
   
}
