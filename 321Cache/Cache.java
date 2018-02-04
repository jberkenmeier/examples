import java.util.NoSuchElementException;

/**
 * This program sets up a simple cache. It has two constructors, one default that sets up cache of size
 * 100, and another loaded constructor that takes an int as a parameter and sets the size of the cache with 
 * the input int value. Various methods for getting/accessing elements, adding/removing, clear and find.
 * 
 * @author joshuaberkenmeier
 *
 * @param <T>
 */

public class Cache<T> implements ICache<T> {
	
	private DLLNode<T> head, tail, dummy;
	private int size, capacity, cacheHit, cacheMiss, cacheAccess;
	private double hitRate, missRate;
	
	/**
	 * Constructor: constructs empty cache with initial capacity of 100.
	 */
	public Cache() 
	{
		dummy = new DLLNode<T>(null); 
		head = dummy;
		tail = dummy;
		size = 0;
		cacheAccess = 0;
		capacity = 100;
	}

	/**
	 * Constructor: constructs empty cache with specified initial capacity
	 */
	public Cache(int cap) 
	{
		dummy = new DLLNode<T>(null); 
		head = dummy;
		tail = dummy;
		size = 0;
		cacheAccess = 0;
		capacity = cap;
	}

	
	@Override
	public T get(T target) 
	{
		DLLNode<T> node = find(target);
		
		//if target found
		if(node != null)
		{
			remove(target);
			add(target);
			cacheHit++;
		}
		
		//if target not in list
		else 
		{
			node = dummy;
			add(target);
			cacheMiss++;
		}
		
		cacheAccess++;
	
		return node.getElement();
	}

	@Override
	public void clear() 
	{
		head = dummy;
		tail = dummy;
		size = 0;
	}

	@Override
	public void add(T element) 
	{
		DLLNode<T> node = new DLLNode<T>(element);
		
		//special case: empty list
		if(isEmpty())
		{
			head = node;
			tail = node;
			node.setNext(dummy);
			dummy.setPrevious(node);
		}
		
		//if list is full
		else if(size == capacity) 
		{
			removeLast();
			node.setNext(head);
			head.setPrevious(node);
			head = node;
		}
		
		else
		{
			node.setNext(head);
			head.setPrevious(node);
			head = node;
		}
		
		size++;
		
	}

	@Override
	public void removeLast() 
	{
		if(isEmpty())
		{
			throw new IllegalStateException("IUDoubleLinkedList - removeLast");
		}
		
		//special case: only one element in list
		if(size == 1)
		{
			head = dummy;
			tail = dummy;
		}
		
		else
		{
			DLLNode<T> previous = tail.getPrevious();
			previous.setNext(dummy);
			dummy.setPrevious(previous);
			tail.setNext(null);
			tail.setPrevious(null);
			tail = previous;
		}
		
		size--;
		
	}


	@Override
	public void remove(T element) 
	{
		DLLNode<T> current = head;
		boolean found = false;
		
		//search for element in list
		while(!found && current != dummy)
		{
			if(current.getElement().equals(element))
			{
				found = true; 
			}
			else
			{
				current = current.getNext(); 
			}
		}
		
		if(!found)
		{
			throw new NoSuchElementException("Cache - remove"); 
		}
		
		DLLNode<T> previous = current.getPrevious();
		DLLNode<T> next = current.getNext(); 
		
		//special case: removing first element in list
		if(current == head)
		{
			next.setPrevious(null);
			current.setNext(null);
			
			//special case: if only one element in list
			if(size == 1)
			{
				head = dummy;
				tail = dummy; 
			}
			else
			{
				head = next;
			}
		}
		else
		{
			previous.setNext(next);
			next.setPrevious(previous); 
			current.setPrevious(null);
			current.setNext(null); 
			
			if(current == tail)
			{
				tail = previous; 
			}
		}
		
		size--;
	}
	@Override
	public void write(T data) 
	{
		DLLNode<T> node = find(data);
		
		if(node == null) 
		{
			throw new NoSuchElementException("Cache: Data not found");
		}
		
		remove(data);
		add(data);
	}

	@Override
	public double getHitRate() 
	{
		hitRate = (double)cacheHit/(double)cacheAccess;
		
		return hitRate;
	}

	@Override
	public double getMissRate() 
	{
		hitRate = (double)cacheHit/(double)cacheAccess;
		missRate = 1 - hitRate;
		
		return missRate;
	}
	
	/**
	 * Method to retrieve the amount of cache hits
	 * @return int cacheHit
	 */
	public int getcacheHits() 
	{
		return cacheHit;
	}
	
	/**
	 * Method to retrieve the amount of cache misses
	 * @return int cacheMiss
	 */
	public int getcacheMiss() 
	{
		return cacheMiss;
	}
	
	/**
	 * Method to retrieve the amount of cache accesses
	 * @return int cacheAccess
	 */
	public int getcacheAccesses() 
	{
		return cacheAccess;
	}

	@Override
	public boolean isEmpty() 
	{
		return size == 0;
	}
	
	/**
	 * Find method searches cache for element. If found returns node containing said element
	 * @param element
	 * @return DLLNode<T>
	 */
	public DLLNode<T> find(T element)
	{
		DLLNode<T> current;
		current = head;
		
		while(current != dummy) 
		{
			if(current.getElement().equals(element)) 
			{
				return current;
			}
			
			current = current.getNext();
		}
		
		return null;
	}
	

}
