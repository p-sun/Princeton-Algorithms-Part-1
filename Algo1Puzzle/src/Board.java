import java.util.Arrays;
//import java.util.Comparator;


public class Board {

	private int[][] tiles;
	private int N = 0;
	
	// construct a tiles from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks) {       
		
		N = blocks.length;
		tiles = new int[N][N];
		
		for (int i = 0; i < N; i++) { // Note: do this for deep copy of 2D array
			tiles[i] = blocks[i].clone(); }
		
	}
	
	public int dimension() { // tiles dimension N
		return N;
	}
	
	public int hamming() { // number of blocks out of place
		int count = 0;
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				
				if (tiles[i][j] != 0 && tiles[i][j] != i*N + j + 1) {
					count += 1; }
				
			}
		}
		
		return count;
	}
	
	// sum of Manhattan distances between blocks and goal
	public int manhattan() { 
		int count = 0;
		int i2, j2; // Where tiles[i][j] should be in the final tiles
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				
				if (tiles[i][j] != 0 ) {
					// i.e. 4 should be in (i2, j2) = (1, 0)
					// 3 should be in (0, 3)
					i2 = tiles[i][j] % N == 0 ? 
						tiles[i][j] / N - 1 : tiles[i][j] / N; 
					j2 = tiles[i][j] % N == 0 ? 
						N-1 : tiles[i][j] % N - 1; 
					count += Math.abs(i2-i) + Math.abs(j2-j);
				}
			}
		}
		return count;
	}

	public boolean isGoal() {               // is this tiles the goal tiles?
		return this.hamming() == 0;
	}

	// A board is obtained by exchanging two adjacent blocks in the same row
	// Every unsolvable configuration can be turned into a solvable one with
		// this exchange
	public Board twin() {
		int[][] twin = new int[N][N];
		
		for (int i = 0; i < N; i++) { // Note: do this for deep copy of 2D array
			twin[i] = tiles[i].clone();
		}
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N-1; j++) {
		
				if (tiles[i][j] == 0 || tiles[i][j+1] == 0) { continue; }
				
				int temp = twin[i][j];
				twin[i][j] = twin[i][j+1];
				twin[i][j+1] = temp;
								
				return new Board(twin);

			}
		}
		
		return new Board(new int[][] {{1,2},{3,0}}); // should never reach here
	}

	public boolean equals(Object y) { // does this tiles equal y?
        if (y == this) return true;
        if (y == null) return false;
		if (y.getClass() != this.getClass() ) { return false; }
		
        Board that = (Board) y;

		return Arrays.deepEquals(this.tiles, that.tiles);
	}
	
	public Iterable<Board> neighbors() {     // all neighboring boards
		Stack<Board> pq = new Stack<Board>();
		
		int i = 0,j = 0; // Find location of the empty space (i, j)
		outerloop:
		for (i = 0; i < N; i++) {
			for (j = 0; j < N; j++) {
				if (tiles[i][j] == 0) { break outerloop; }
			}
		}
		
		if (i - 1 >= 0) { pq.push(this.move(i, j, i-1, j)); }
		if (i + 1 < N) 	{ pq.push(this.move(i, j, i+1, j)); }
		if (j - 1 >= 0) { pq.push(this.move(i, j, i, j-1)); }
		if (j + 1 < N) 	{ pq.push(this.move(i, j, i, j+1)); }
		
		return pq;
	}

	// (k, l) is the tile you're moving into the empty space (i, j)
	private Board move(int i, int j, int k, int l) {
		int[][] newTiles = new int[N][N];
		
		for (int n = 0; n < N; n++) { // Note: do this for deep copy of 2D array
			newTiles[n] = tiles[n].clone();
		}
		
		newTiles[i][j] = newTiles[k][l];
		newTiles[k][l] = 0;
		
		return new Board(newTiles);
		
	}
	
	public String toString() {
		String s = "";
		s += N + "\n";
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				 s += String.format("%2d ", tiles[i][j]);
			}
			s += "\n";
		}
		
		return s;
		
	}
	
	public static void main(String[] args) {
		int[][] k = new int[][] {
			{1,2,3},
			{4,0,6},
			{7,8,5},
		};
			
		Board b = new Board(k);
		
		StdOut.println("Initial: " + b.toString());
		StdOut.println("Hamming: " + b.hamming());
		StdOut.println("Manhattan: " + b.manhattan());

		for (Board neib : b.neighbors()){
			StdOut.println("iteration: " + neib);
		}
		
		StdOut.println("Twin: \n" + b.twin());
		
		int[][] l = new int[][] {
				{1,2,3},
				{4,0,6},
				{7,8,5},
			};
		Board a = new Board(l);

		StdOut.println("equals tiles: " + Arrays.deepEquals(k, l));
		StdOut.println("equals boards: " + b.equals(a));

		
	}

}
