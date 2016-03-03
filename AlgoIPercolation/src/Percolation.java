
public class Percolation {
    
    
    private int[][] grid;
    // uses 3 bitwise flags. 001 (1) is open. 010 (2) is connected to bottom. 100 (4) is connected to top
    // bitwise tutorial http://www.tutorialspoint.com/java/java_bitwise_operators_examples.htm
    
	private  WeightedQuickUnionUF uf; 
	//0th index is top virtual node. (N+1)th index is bottom virtual node
	
    private int length; //N
    
    private boolean percolates = false;
    
    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
    	if (N <= 0) throw new java.lang.IllegalArgumentException("percolation constructor needs positive N");
    	
    	length = N;
        grid = new int[N+1][N+1]; 
        uf = new WeightedQuickUnionUF(N*N+1);
        // Flag of virtual 0th's top root is stored at (i = 0, j = length)
        	// decided because rowI(0) = 0, colJ(0) = length
        
        ///auto assigned 0s to everything
        for (int i = 0; i < N+1; i++) {
            for (int j = 0; j < N+1; j++) {
                grid[i][j] = 0;
            }
        }
    }
    
    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
		//System.out.println("perc.open");

    	checkIndex(i, j);

    	if ((grid[i][j] & 1) != 1) { // If closed, need to open            

    		grid[i][j] = grid[i][j] | 1; // open the grid

        	// Root(i, j) is always always itself since it was just closed.
    		//   For any of the roots of trees beside it, may be 
    		// 		FULL(1)
    		// 		CONNECTED-TO-BOTTOM(2) 
    		 //		CONNECTED-TO-TOP(4)
       		//No way to find out which tree will be the parent of the other
       		//	thus, must make sure all TRUE flags are transferred over any unions 
    		
    		
    		// Union to virtual top if first row is opened -----------
            if (i == 1) {
            	grid[i][j] = grid[i][j] | 4; // set connec-to-top flag = true
            	combineFlags(i, j, 0, length);            	
            	uf.union(0, id(i, j));
            }  
            // Connect to all 4 adjacent grids ---------------
            if (i > 1 && (grid[i-1][j] & 1) == 1) { // TOP of [i][j] if it's open
            	//System.out.println("top");

            	combineFlags(i, j, i-1, j);            	
            	uf.union(id(i, j), id(i-1, j));
            } 
            if (i < length && (grid[i+1][j] & 1) == 1) {    // BOTTOM
            	//System.out.println("bottom");

            	combineFlags(i, j, i+1, j);            	
            	uf.union(id(i, j),  id(i+1, j));
            }
            if (j > 1 && (grid[i][j-1] & 1) == 1) {         // LEFT
            	//System.out.println("left");

            	combineFlags(i, j, i, j-1);            	
            	uf.union(id(i, j), id(i, j-1));
            } 
            if (j < length && (grid[i][j + 1] & 1) == 1) {  //RIGHT
            	//System.out.println("right");

            	combineFlags(i, j, i, j+1);            	
            	uf.union(id(i, j), id(i, j+1));
            }
            
            // If it doesn't percolate yet, find out whether it does ---------------
            if (!percolates) { 
            	
            	int rootAfterUnions = uf.find(id(i, j));
            	int rooti2 = rowI(rootAfterUnions);
            	int rootj2 = colJ(rootAfterUnions);

        		// if [i][j] is on the last row, set the root's bottom-connectiveness flag to true.
            	if (i == length) { 
            		grid[rooti2][rootj2] = grid[rooti2][rootj2] | 2;
            	} 
            	
              	//System.out.println("rootafterunion: " + rootAfterUnions);
            	//System.out.println("connect to top: " + ((grid[rooti2][rootj2] & 4) == 4));
            	//System.out.println("connect to bottom: " + ((grid[rooti2][rootj2] & 2) == 2));
            	//System.out.println("----- int7 connected to bom: " + ((7 & 2) == 2));

            	
            	// If root is connected to both top and bottom, set percolates to true
            	if ((grid[rooti2][rootj2] & 2) == 2 && (grid[rooti2][rootj2] & 4) == 4) { 
            		percolates = true;            		
            	}
            	
            }
        } 
    }
    
    private int rowI(int p) { // To do: maybe change the if to == 0 and p/length+1? 
    	int rowI = p / length; 	
    	if (p % length != 0) { rowI++; }
    	return rowI;
    }
    private int colJ(int p) {
    	int colJ = p % length;    
    	if (p % length == 0) { colJ += length; }
    	return colJ;
    }

    private void combineFlags(int i, int j, int k, int l) {
    	//System.out.println("combining flags. i " + i + " j " + j + " k " + k + " l " + l);
    	int rootOfSideGrid = uf.find(id(k, l));
    	int rootk = rowI(rootOfSideGrid);
    	int rootl = colJ(rootOfSideGrid);
    	
    	int rootij = uf.find(id(i, j));
    	int rooti = rowI(rootij);
    	int rootj = colJ(rootij);
    	
    	//System.out.println("grid[i][j]: " + grid[i][j] + " grid[rootk][rootl]: " + grid[rootk][rootl]);
    	
    	int combinedFlags = grid[rooti][rootj] | grid[rootk][rootl];
    	
    	//System.out.println("combined flags: " + combinedFlags);

    	grid[rooti][rootj] = combinedFlags;
    	grid[rootk][rootl] = combinedFlags;
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
    	checkIndex(i, j);

        return ((grid[i][j] & 1) == 1);
    }
    
    // is site (row i, column j) full? // Visualized by blue. meaning it's connected to top
    // Called for every square for visualization purposes
    // All methods should take constant time plus a constant number of calls 
    	// to the union-find methods union(), find(), connected(), and count().
    //  use multiple virtual bottoms (max N/2), 
    // made use of 2 WeightedQuickUnionUFs,  
    	// one to check if it percolates, the other to check for whether the cells are full
    public boolean isFull(int i, int j) {
    	checkIndex(i, j);
    	
    	// Instead of this, check if the root of that component is connected to 0.
    	
    	return (uf.connected(0, id(i, j)));
    }
    
    // does the system percolate?
    public boolean percolates() {
    	return percolates;
    }
    
    private void checkIndex(int i, int j) {
    	if (i <= 0 || i > length) throw new IndexOutOfBoundsException("row index i out of bounds");
    	if (j <= 0 || j > length) throw new IndexOutOfBoundsException("row index j out of bounds");
    }
    
    // Given i and j, return the id of that grid
    private int id(int i, int j) {
    	return j+(i-1)*length;
    }
    
    // test client (optional)
    public static void main(String[] args) {
       int N = 3;   
      Percolation perc = new Percolation(N);

      //perc.open(2, 7); // throws exception

      perc.open(2, 3);
      System.out.println("grid(2, 3) is open: " + perc.isOpen(2, 3)); 
    }
}