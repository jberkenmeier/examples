import java.util.Arrays;

/**
 * 
 * This class constructs a max-heap data structure
 * 
 * 
 * @author joshberkenmeier
 *
 * @param <T>
 */
public class MaxHeap<T> {
	
	
	
	
	private HeapNode<T>[] heap;
	private int heapSize, capacity;
	private int DEFAULT_CAPACITY = 50;
	
	/**
	 * Default Constructor
	 */
	@SuppressWarnings("unchecked")
	public MaxHeap()
	{
		heapSize = 0;
		capacity = DEFAULT_CAPACITY;
		heap = new HeapNode[capacity + 1];
		
	}
	
	/**
	 * Constructor: takes in objects and keys, and builds a max-heap
	 * @param objects
	 * @param keys
	 */
	@SuppressWarnings("unchecked")
	public MaxHeap(T[] objects, int[] keys) 
	{
		capacity = DEFAULT_CAPACITY;
		heapSize = objects.length;
		heap = new HeapNode[capacity + 1];
		
		for(int i = 0; i < objects.length; i++) 
		{
			HeapNode<T> heapNode = new HeapNode<T>(objects[i], keys[i]);
			heap[i + 1] = heapNode;
		}
		
		for(int i = objects.length/2 ; i > 0; i--) 
		{
			maxHeapify(i);
		}
		
		
	}
	
	/**
	 * Returns the max object
	 * @return T- max object
	 */
	public T heapMax() 
	{
		if(isEmpty()) 
		{
			return null;
		}
		
		else 
		{
			HeapNode<T> heapNode = heap[1];
			return heapNode.getObject();
		}
	}
	
	/**
	 * Extracts, then returns the max object
	 * @return object
	 */
	public T extractHeapMax() 
	{
		HeapNode<T> max;
		
		if(isEmpty()) 
		{
			return null;
		}
		
		else 
		{
			
			//grab max value object for return
			max = heap[1];
			
			//replace object at top of heap, with object at the end of heap
			heap[1] = heap[heapSize];
			
			//remove last object, reduce size
			heap[heapSize] = null;
			heapSize--;
			
			for(int i = 1; i < heapSize; i++) 
			{
				maxHeapify(i);				
			}
			
			return max.getObject();
			
		}
	
	}
	
	/**
	 * Takes an index and key value as parameters, changes the key value of the object at specified index to specified key value.
	 * Moves object to correct position in heap.
	 * @param index - int
	 * @param key - int
	 */
	public void increaseHeapKey(int index, int key) 
	{
		int current = index;
		
		if (key > heap[current].getKey())
		{
			heap[current].setKey(key);
		}
		
			
		while(current > 1 && heap[parent(current)].getKey() < heap[current].getKey()) 
		{
			exchange(current, parent(current));
			current = parent(current);
		}
	}
	
	/**
	 * Inserts a new object into the heap. Moves object to correct position in heap.
	 * @param object - T
	 * @param key - int
	 */
	public void maxHeapInsert(T object, int key) 
	{
		if(heapSize == capacity) 
		{
			expandCapacity();
		}
		
		HeapNode<T> newElement = new HeapNode<T>(object, key);
		
		heap[heapSize + 1] = newElement;
		heapSize++;
		
		moveUp(heapSize);
	}
	
	/**
	 * Expands the capacity of your heap by x2
	 */
	private void expandCapacity() 
	{
		capacity = capacity * 2;
		heap = Arrays.copyOf(heap, capacity);
	}
	
	/**
	 * Takes the object at specified index, and moves it to a correct position within the heap
	 * @param index - int
	 */
	public void maxHeapify(int index) 
	{
		int leftChild = left(index);
		int rightChild = right(index);
		int current = index;
		int largest = 0;
		
		if(leftChild <= heapSize && heap[leftChild].getKey() > heap[current].getKey()) 
		{
			largest = leftChild;
		}
		
		else 
		{
			largest = current;
		}
		
		if(rightChild <= heapSize  && heap[rightChild].getKey() > heap[largest].getKey()) 
		{
			largest = rightChild;
		}
		
		if(largest != current) 
		{
			exchange(current, largest);
			maxHeapify(largest);
		}
	}
	
	/**
	 * Takes the object at specified index and moves it up to correct position in heap
	 * @param index - int
	 */
	private void moveUp(int index) 
	{
		int parent = parent(index);
		int current = index;
		
		while(parent != 0 && heap[parent].getKey() < heap[current].getKey()) 
		{
			exchange(parent, current);
			
			current = parent;
			parent = parent(current);
		}
		
	}
	
	/**
	 * Gets size of heap
	 * @return heapSize - int
	 */
	public int getHeapSize() 
	{
		return heapSize;
	}
	
	/**
	 * Checks to see if heap is empty
	 * @return boolean
	 */
	public boolean isEmpty() 
	{
		return heapSize == 0;
	}
	
	/**
	 * Exchanges the object at index1 with the object at index2
	 * @param index1 - int
	 * @param index2 - int
	 */
	private void exchange(int index1, int index2) 
	{
		HeapNode<T> dummy = heap[index1];
		heap[index1] = heap[index2];
		heap[index2] = dummy;
	}
	
	/**
	 * Finds the left child index of object at index
	 * @param index - int
	 * @return left child index - int
	 */
	private int left(int index) 
	{
		return (index * 2);
	}
	
	/**
	 * Finds the right child index of object at index
	 * @param index - int
	 * @return right child index - int
	 */
	private int right(int index) 
	{
		return (index * 2) + 1;
	}
	
	/**
	 * Finds the parent index of the object at index
	 * @param index - int
	 * @return parent index - int
	 */
	private int parent(int index) 
	{
		return (index/2);
	}
	
	/**
	 * Sets the size of your heap
	 * @param size - int
	 */
	@SuppressWarnings("unused")
	private void setHeapSize(int size) 
	{
		heapSize = size;
	}
	
	/**
	 * Sets the capacity of your heap
	 * @param cap - int
	 */
	@SuppressWarnings("unused")
	private void setCapacity(int cap) 
	{
		capacity = cap;
	}
}
