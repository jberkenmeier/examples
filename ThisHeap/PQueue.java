/**
 * This class creates a priority queue data structure using a max heap
 * @author jberk
 *
 * @param <T>
 */
public class PQueue<T> {
	
	
	private MaxHeap<T> maxHeap;
	
	/**
	 * Default constructor, makes a priority queue with an empty max heap
	 */
	public PQueue() 
	{
		maxHeap = new MaxHeap<T>();
	}
	
	/**
	 * Loaded constructor, creates a priority queue with a max heap that has the parameters specified
	 * @param objects - array of objects
	 * @param keys - array of key values
	 */
	public PQueue(T[] objects, int[] keys) 
	{
		maxHeap = new MaxHeap<T>(objects, keys);
	}

	/**
	 * Returns the max process
	 * @return maxHeap.heapMax()
	 */
	public T maximum() 
	{
		return maxHeap.heapMax();
	}
	
	/**
	 * Extracts the max process, and returns the value
	 * @return maxHeap.extractHeapMax()
	 */
	public T extractMax() 
	{
		return maxHeap.extractHeapMax();
	}
	
	/**
	 * Increases the key value of process at specified index
	 * @param index - int
	 * @param key - int
	 */
	public void increaseKey(int index, int key) 
	{
		maxHeap.increaseHeapKey(index, key);
	}
	
	/**
	 * Inserts a process into your queue with given key value
	 * @param object - T
	 * @param key - int
	 */
	public void insert(T object, int key) 
	{
		maxHeap.maxHeapInsert(object, key);
	}
	
	/**
	 * Checks to see if priority queue is empty
	 * @return boolean
	 */
	public boolean isEmpty() 
	{
		return maxHeap.isEmpty();
	}
	
	/**
	 * Returns size of priority queue
	 * @return int 
	 */
	public int size() 
	{
		return maxHeap.getHeapSize();
	}
}
