

public class BinarySearch {

	public static int binarySearch(int[] a, int key) {
		int lo = 0, hi = a.length-1;
		
		while (lo <= hi){
			int mid = lo + (hi - lo)/2;

			if (key < a[mid]) hi = mid-1;
			else if (key > a[mid]) lo = mid+1;
			else return mid;
		}
		return -1;
	}
	
	
	public static void main(String[] args) {
		System.out.println("ASDF");
		int[] a = {0,2,4,5,7,23,59};
		System.out.println("Searching for 3: " + binarySearch(a,3));
		System.out.println("Searching for 23: " + binarySearch(a,23));
	}

}
