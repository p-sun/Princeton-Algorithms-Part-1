// A randomized queue is similar to a stack or queue, except that 
// the item removed is chosen uniformly at random from items 
// in the data structure. 

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item>{

	private int size = 0; // points to the next empty space
	private Item[] a;
	
	// constructor
	public RandomizedQueue(){
		a = (Item[]) new Object[10];
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public int size() {
		return size;
	}
	
	public void enqueue (Item item) {
		
		if (item == null) throw new java.lang.NullPointerException(
				"Attempted to enqueue null item");
		
		if (++size > a.length) resize(true);
		
		int rand = StdRandom.uniform(0,size); 
			// size must be > 0 for .uniform to work
			// returns int from 0 inclusive, ++size exclusive)
		//StdOut.println("rand: " + rand);
		
		Item toSwitch = a[rand];
		//StdOut.println("toS: " + toSwitch);

		a[rand] = item;
		if (rand != size-1)
			a[size-1] = toSwitch;
		//StdOut.println("size: " + size);

	}
	
	private void resize(boolean bigger) {
		//StdOut.println("resize");
	
		Item[] olda = a;
		int i = 0;
		if (bigger)
			a = (Item[]) new Object[a.length*2];
		else
			a = (Item[]) new Object[a.length/2+3];
		
		for (Item item: olda){
			if (i >= size) break;
			a[i++] = item;
		}
	}
	
	public Item dequeue() {
		if (isEmpty()) throw new java.util.NoSuchElementException(
				"Cannot deque empty queue.");
		if (size < a.length/2 && size > 50) resize(false);
		int rand = StdRandom.uniform(0,size); 
		Item toReturn = a[rand];
		a[rand] = a[--size]; 
		return toReturn;
		
	}
	
	public Item sample() {
		if (isEmpty()) throw new java.util.NoSuchElementException(
				"Cannot sample empty queue.");
		
		int rand = StdRandom.uniform(0,size); 
		return a[rand];
	}
	
	
	public Iterator<Item> iterator() { return new ListIterator(); }
	
	// return an independent iterator over items in random order
	private class ListIterator implements Iterator<Item> {
        //private Item[] ra; // random a
        RandomizedQueue<Item> ra = new RandomizedQueue<Item>();
        
        int itemsLeft = size;
              
    	private ListIterator () {
    		for (Item i: a) {
    			if (itemsLeft-- > 0)
    				ra.enqueue(i);
            }
    	}
        
        public boolean hasNext() {  
        	return ra.size > 0;
        }
        
        public Item next() {
        	if (ra.size == 0) 
        		throw new java.util.NoSuchElementException(
        				"There is no more .next() in the iterator.");
        	return ra.dequeue();
        }

        public void remove() { 
        	throw new java.lang.UnsupportedOperationException(
        		"remove operation is not suppported in this iterator.");}
    }
	
	public static void main(String[] args) {
		RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();
		
		q.enqueue(1);
		q.enqueue(2);
		q.enqueue(3);
		q.enqueue(4);
		q.enqueue(5);
		
		for (Integer i: q) {
			System.out.print(i);
		}
		System.out.println("\nIterator#2:");
		for (Integer i: q) {
			System.out.print(i);
		}
				
		System.out.println("\n\nsize: " + q.size());
		
		System.out.println("sample: " + q.sample());
		
		System.out.println("dequeue: " + q.dequeue());
		System.out.println("dequeue: " + q.dequeue());
		System.out.println("dequeue: " + q.dequeue());
		System.out.println("dequeue: " + q.dequeue());
		System.out.println("dequeue: " + q.dequeue());
		// System.out.println("dequeue: " + q.dequeue()); // throws exception
		
		System.out.println("\n");
		System.out.println("size: " + q.size());
		for (Integer i: q) {
			System.out.print(i);
		}
		
		q.enqueue(49);
		System.out.println("\n\nsize: " + q.size());

		System.out.println("dequeue: " + q.dequeue());
	}
}
