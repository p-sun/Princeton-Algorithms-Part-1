

public class QuickUnionUF {
	private int[] id;
	
	//private int[] size;
	// stores the branch size from item to root for the **weighted quickUnion**
	// p branch size is stored in size[0], q branch size is stored in size[1]
	
	public QuickUnionUF(int N) {
		id = new int[N];
		for (int i = 0; i < N; i++)
			id [i] = i;
	}
	
	
	private int root(int i) { 
		while (i != id[i]){
			return root(id[i]);  
		}
		return i;
	}
	
	public boolean connected(int p, int q) {
		return root(p) == root(q);
	}
	
	public void union(int p, int q) {
		int i = root(p);
		int j = root(q);
		id[i] = j;
	}

	 public static void main(String[] args) {
	        QuickUnionUF uf = new QuickUnionUF(10);
	        uf.union(3, 4);
	        uf.union(2, 3);
	        uf.union(1, 2);

	        System.out.println("1 is connected to 4: " + uf.connected(1, 4));

	        for (int i = 0; i < 10; i++)
	        	System.out.print(uf.id[i] + " ");
	 }

	
}
