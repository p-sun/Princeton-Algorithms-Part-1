import java.util.Comparator;
// To give yourself more memory space, add     -Xmx1600m       to 
	// run -> run configurations -> VM arguments

public class Solver {

	private MinPQ<SearchNode> pq;
	private SearchNode min, tMin;
	private MinPQ<SearchNode> twin;
	
	private static class SearchNode {
		
		private Board board;
		private int man; //mahattan #
		private int moves; // Number of moves to get to the board
		private SearchNode previous;
		
		private SearchNode(SearchNode prev, Board b, int mov) {
			previous = prev;
			board = b; 
			moves = mov;
			man = b.manhattan();
			//if (previous != null) { moves = previous.moves + 1; } //otherwise moves = 0
		}
		
	    private static final Comparator<SearchNode> BY_PRIORITY = new Comparator<SearchNode>() {
	    	public int compare(SearchNode p, SearchNode q) {
	    		
	    		// Use <= because 
	    		if (p.man + p.moves <= q.man + q.moves ) return -1;
	    		return 1;
	    		
	    		/*	    		 
	    		if (p.man + p.moves == q.man + q.moves) {
	    		 
	    			if (p.man < q.man) return -1;
	    			else if (p.man == q.man) return 0;
	    			else return 1;
	    		} else if (p.man + p.moves < q.man + q.moves) return -1;
	    		return 1;
	    		/*
	    		
	    		/*
	    		if (p.man == q.man) {
	    			if (p.moves <= q.moves) return -1;
	    			else return 1;
	    		} else if (p.man < q.man) return -1;
	    		return 1; // If p.man > q.man
	    		*/ 
	    	}
	    };
	   
	}
	
	// find a solution to the initial board (using the A* algorithm)
    // delete from the priority queue the search node with the minimum priority, 
    // and insert onto the priority queue all neighboring search nodes 
    // (those that can be reached in one move from the dequeued search node). 
    // Repeat this procedure until the search node dequeued corresponds to a goal board.
    public Solver(Board initial) {
    	
    	if (initial == null) {
    		throw new java.lang.NullPointerException("initial board is null."); }

    	
    	// Insert initial SearchNode into priority queue ------------------------
    	pq = new MinPQ<SearchNode>(SearchNode.BY_PRIORITY);	// initialize w a comparator    	
    	min = new SearchNode(null, initial, 0);
    	//pq.insert(min);
    			
    	twin = new MinPQ<SearchNode>(SearchNode.BY_PRIORITY);
    	tMin = new SearchNode(null, initial.twin(), 0);
    	twin.insert(tMin);
    	
    	// Find board with manhattan value of 0 -------------------------------
    	while(min.man != 0 && tMin.man != 0) {
    		
//    		StdOut.println("man: " + min.man + " moves: " + min.moves);
//    		StdOut.println("min: " + min.board);

    		for (Board nBoard : min.board.neighbors()) {
    			
    			//don't enqueue a neighbor if its board is the same as 2 steps before
    			if (min.previous == null || 
    				min.previous != null && !nBoard.equals(min.previous.board)) {
 //   	    		StdOut.println(nBoard);
    				pq.insert(new SearchNode(min, nBoard, min.moves+1)); }
 
    		}
    		min = pq.delMin();
    		
    		for (Board nBoard : tMin.board.neighbors()) {
    			if (tMin.previous == null || 
        				tMin.previous != null && !nBoard.equals(tMin.previous.board)) {
    				twin.insert(new SearchNode(tMin, nBoard, tMin.moves+1)); }
    		}
    		tMin = twin.delMin();
    	}
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {    	
    	return min.man == 0;
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
    	if (min.man != 0) return -1;
    	return min.moves;
    }
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
    	if (!this.isSolvable()) return null;
    	
    	Stack<Board> stack = new Stack<Board>();
    	
    	SearchNode node = min;
    	while (node != null) {
    	   	stack.push(node.board);
    	   	node = node.previous;
    	}
    	
    	return stack;
    }
    
    // solve a slider puzzle from file
	public static void main(String[] args) {
		
		// create initial board from file
	    In in = new In(args[0]);
	    //In in = new In("datafiles/puzzle3x3-28.txt");
	    //In in = new In("datafiles/puzzle4x4-unsolvable.txt");
    

	    int N = in.readInt();
	    int[][] blocks = new int[N][N];
	    for (int i = 0; i < N; i++)
	        for (int j = 0; j < N; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);

	    // solve the puzzle
	    Solver solver = new Solver(initial);

	    // print solution to standard output
	    if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }
		
	}

}
