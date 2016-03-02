
public class PercolationStats {

	private double[] arr; 
		// arr[T] is the fraction of open calls where percolation occurred
	private int times;
	private double length;
	
	public PercolationStats(int N, int T) {     // perform T independent experiments on an N-by-N grid
		if (N <= 0 || T <= 0) {
			throw new java.lang.IllegalArgumentException("N or T is out of bounds for PercolationStats");
		}
		   
		arr = new double[T];
		times = T;
		length = N;
		
		for (int i = 0; i < T; i++) {

			boolean hasPercolated = false;
			Percolation perc = new Percolation(N);
			int randi, randj;
			
			for (int opened = 1; opened <= N*N; opened++) { // open up to N*N times.
		
				randi = StdRandom.uniform(1, N+1);
				randj = StdRandom.uniform(1, N+1);
				while (perc.isOpen(randi, randj)) {
					randi = StdRandom.uniform(1, N+1);
					randj = StdRandom.uniform(1, N+1);
				}
				
				perc.open(randi, randj);				
	
				//System.out.println("opened "+ opened);
				
				hasPercolated = perc.percolates();
				if (hasPercolated) {

					arr[i] = opened/length/length;
					//System.out.println("arr[" + i + "] = " + arr[i]);
					break;
				}
			}
		}		   
	}
		
	public double mean() {                      // sample mean of percolation threshold
		return StdStats.mean(arr);
	}
		   
	public double stddev() {                    // sample standard deviation of percolation threshold
		if (times == 1) { return Double.NaN; }
		return StdStats.stddev(arr);
	}
		   
	public double confidenceLo() {              // low  endpoint of 95% confidence interval
		return mean() - stddev()*1.96/Math.sqrt(times);
	}
		   
	public double confidenceHi() {              // high endpoint of 95% confidence interval
		return mean() + stddev()*1.96/Math.sqrt(times);	
	}
		
	public static void main(String[] args) {
		
		int N = 0;
		int T = 0;
		N = Integer.parseInt(args[0]);
        T = Integer.parseInt(args[1]);
		
		/*
		if (args.length > 0) {
		    try {
		        N = Integer.parseInt(args[0]);
		        T = Integer.parseInt(args[1]);
		    } catch (Exception e) {
		        System.err.println("Argument error. Please check that args N and T are both integers.");
		        System.exit(1);
		    }
		}*/
				
		PercolationStats stats = new PercolationStats(N, T);
//		PercolationStats stats = new PercolationStats(10, 8);
		System.out.println("mean                    = " + stats.mean());
		System.out.println("stddev                  = " + stats.stddev());
		System.out.println("95% confidence interval = " + stats.confidenceLo()
				+ ", " + stats.confidenceHi());
	}

}
