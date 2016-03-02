import java.util.Iterator;

// Dequeue. A double-ended queue or deque (pronounced "deck") 
// is a generalization of a stack and a queue that supports adding and removing 
// items from either the front or the back of the data structure.


public class Deque<Item> implements Iterable<Item> {

	private Node first, last = null;
	private int size = 0;
	
	private class Node {
		Item item;
		Node next, previous = null;
	}
	
	// construct an empty deque
	public Deque() {
		
	}
	
	// is the deque empty?
	public boolean isEmpty() {
		return size == 0;
	}
	
	// return the number of items on the deque
	public int size() {                        
		return size;
	}
	
	// add the item to the front
	public void addFirst(Item item) {
		checkItemIsValid(item);
		Node oldFirst = first;
	    first = new Node();
	    first.item = item;
	    first.next = oldFirst;
	    
	    if (oldFirst == null) 
	    	last = first;
	    else
	    	oldFirst.previous = first;
	    	
	    size++;
		
	}
	
	// add the item to the end
	public void addLast(Item item) {  
		checkItemIsValid(item);
		Node oldLast = last;
		last = new Node();
		last.item = item;
		last.previous = oldLast;
		
		if (oldLast == null)
			first = last;
		else
			oldLast.next = last;
		
		size++;
	}
	
	private void checkItemIsValid(Item item) {
		if (item == null) {
			throw new java.lang.NullPointerException("Cannot add null item.");
		} 
	}
	
	// remove and return the item from the front
	public Item removeFirst() {
		if (size == 0)
			throw new java.util.NoSuchElementException(
					"Cannot remove item from empty deque.");
		else {
			Item returnItem = first.item;
			first = first.next;
			
			if (first == null) 
				last = null;
			else
				first.previous = null;
			
			size--;
			return returnItem;
		}
	}
	
	// remove and return the item from the end
	public Item removeLast() {
		if (size == 0)
			throw new java.util.NoSuchElementException(
					"Cannot remove item from empty deque.");
		else {
			Item returnItem = last.item;
			last = last.previous;
			
			if (last == null)
				first = null;
			else
				last.next = null;
		
			size--;
			return returnItem;
		}
		
	}
	
	public Iterator<Item> iterator() { return new ListIterator(); }
	
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        
        public boolean hasNext() {  
        	return current != null;  }
        
        public Item next() {
        	if (current == null) { 
        		throw new java.util.NoSuchElementException(
        				"Called .next() when there's no next item.");
        	} 
        	Item item = current.item;
        	current   = current.next;
        	return item;
        }
        
        public void remove() {
        	throw new java.lang.UnsupportedOperationException(
        			"remove method in this iterator is not supported."); 
        }
    }
	
	public static void main(String[] args) {
		Deque<Integer> deq = new Deque<Integer>();
	
		deq.addFirst(1);
		deq.addFirst(2);
		deq.addFirst(3);
		deq.addFirst(4);
		deq.addFirst(5);

		// Using the iterator. Method 1
		for (Integer i : deq){
			StdOut.print(i);
		}
		StdOut.println("\nMethod 2 Below:");
		
		// Using the iterator. Method 2
		Iterator<Integer> itr = deq.iterator();
		while (itr.hasNext()){
			Object i = itr.next();
			System.out.print(i);
		}
		StdOut.println("\n");
	
		//deq.addFirst(null); //throws excep

		StdOut.println(deq.removeFirst());		
		StdOut.println(deq.removeLast());
		StdOut.println(deq.removeFirst());
		StdOut.println(deq.removeLast());
		StdOut.println(deq.removeLast());
		// StdOut.println(deq.removeLast()); //throws excep
		
	}

}
