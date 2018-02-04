import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T>
{
	private DLLNode<T> head, tail, dummy;
	private int size, modCount;
	
	//constructor, initialize variables
	public IUDoubleLinkedList()
	{
		dummy = new DLLNode<T>(null); 
		head = dummy;
		tail = dummy;
		modCount = 0; 
		size = 0; 
	}

	@Override
	public void addToFront(T element) 
	{
		DLLNode<T> node = new DLLNode<T>(element);
		
		//special case: empty list
		if(isEmpty())
		{
			dummy.setPrevious(node);
			node.setNext(dummy);
			head = node;
			tail = node;
		}
		
		else
		{
			node.setNext(head);
			head.setPrevious(node);
			head = node;
		}
		
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) 
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
		
		else
		{
			node.setNext(dummy);
			node.setPrevious(tail);
			dummy.setPrevious(node);
			tail.setNext(node);
			tail = node;
		}
		
		size++;
		modCount++;
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
		
		else
		{
			node.setNext(dummy);
			node.setPrevious(tail);
			dummy.setPrevious(node);
			tail.setNext(node);
			tail = node;
		}	
		
		size++;
		modCount++;
	}

	@Override
	public void addAfter(T element, T target) 
	{
		DLLNode<T> node = new DLLNode<T>(element);
		
		int index = 0;
		boolean found = false; 
		DLLNode<T> targetNode = head;
		
		// search list until element found or reach end of array 
		while(!found && index < size)
		{
			if(targetNode.getElement() == target)
			{
				found = true; 
			}
			else
			{
				targetNode = targetNode.getNext();
				index++; 
			}
		}
		// if not found, throw exception 
		if(!found)
		{
			throw new NoSuchElementException("IUDoubleLinkedList - addAfter");
		}
		
		//special case: if only one element in list or at end of list
		if(size == 1 || index == size-1)
		{
			node.setNext(dummy);
			node.setPrevious(targetNode);
			dummy.setPrevious(node);
			targetNode.setNext(node);
			tail = node;
		}
		
		else
		{
			DLLNode<T> temp = targetNode.getNext();
			node.setNext(temp);
			node.setPrevious(targetNode);
			temp.setPrevious(node);
			targetNode.setNext(node);
		}
		
		size++;
		modCount++;			
	}

	@Override
	public void add(int index, T element) 
	{
		if(index < 0 || index > size)
		{
			throw new IndexOutOfBoundsException("IUDoubleLinkedList - add(index)");
		}
		
		DLLNode<T> node = new DLLNode<T>(element);
		
		//special case: if adding at front of list
		if(index == 0)
		{
			
			if(isEmpty())
			{
				node.setNext(dummy);
				dummy.setPrevious(node);
				head = node;
				tail = node;
			}
			else
			{
				node.setNext(head);
				head.setPrevious(node);
				head = node;
			}
		}
		
		//special case: if adding at end of list
		else if(index == size)
		{
			if(isEmpty())
			{
				node.setNext(dummy);
				dummy.setPrevious(node);
				head = node;
				tail = node;
			}
			
			else
			{
				node.setNext(dummy);
				node.setPrevious(tail);
				tail.setNext(node);
				dummy.setPrevious(node);
				tail = node;
			}
		}
		
		else
		{
			DLLNode<T> current = head;
			
			for(int i = 0; i < index; i++)
			{
				current = current.getNext(); 
			}
			DLLNode<T> previous = current.getPrevious();
			previous.setNext(node);
			current.setPrevious(node);
			node.setNext(current);
			node.setPrevious(previous);
		}
		
		size++; 
		modCount++; 
		
	}

	@Override
	public T removeFirst() 
	{
		if(isEmpty())
		{
			throw new IllegalStateException("IUDoubleLinkedList - removeFirst");
		}
		
		DLLNode<T> returnElement = head;
		DLLNode<T> temp = head.getNext();
		
		temp.setPrevious(null);
		head.setNext(null);
		
		//special case: only one element in list
		if(size == 1)
		{
			head = dummy;
			tail = dummy;
		}
		
		else
		{
			head = temp;
		}
		

		size--;
		modCount++;

		return returnElement.getElement();
	}

	@Override
	public T removeLast() 
	{
		if(isEmpty())
		{
			throw new IllegalStateException("IUDoubleLinkedList - removeLast");
		}
		
		DLLNode<T> returnTemp = tail;
		
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
		modCount++;
		return returnTemp.getElement();
	}

	@Override
	public T remove(T element) 
	{
		DLLNode<T> current = head;
		boolean found = false;
		
		//search for element in list
		while(!found && current != dummy)
		{
			if(current.getElement() == element)
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
			throw new NoSuchElementException("IUDoubleLinkedList - remove"); 
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
		modCount++;
		
		return element;
	}

	@Override
	public T remove(int index) 
	{
		if(index < 0 || index >= size())
		{
			throw new IndexOutOfBoundsException("IUDoubleLinkedList - remove(index)");
		}
		
		DLLNode<T> current = head;
		boolean found = false;
		int i = 0;
		
		//search for index, if found grab node at that index
		while(!found && i < size)
		{

			if (index == i)
			{

				found = true;
			}

			else
			{
				current = current.getNext();
				i++;
			}
		}
		if(!found)
		{
			throw new NoSuchElementException("IUDoubleLinkedList - remove(index)");
		}
		
		DLLNode<T> previous = current.getPrevious();
		DLLNode<T> next = current.getNext(); 
		
		//special case: removing first element in list
		if(index == 0)
		{
			next.setPrevious(null);
			current.setNext(null);
			
			//special case: only one element in list
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
		modCount++; 

		return current.getElement();
	}

	@Override
	public void set(int index, T element) 
	{
		if(index < 0 || index >= size()) 
		{
			throw new IndexOutOfBoundsException("IUDoubleLinkedList - set"); 
		}

		DLLNode<T> current = head; 
		//iterate through list to get to node at specified index
		for(int i = 0; i < index; i++)
		{
			current = current.getNext(); 
		}
		current.setElement(element);
		
		modCount++;		
	}

	@Override
	public T get(int index) 
	{
		if(index < 0 || index >= size()) 
		{
			throw new IndexOutOfBoundsException("IUDoubleLinkedList - get"); 
		}

		DLLNode<T> current = head; 
		
		//iterate through list to get to node at specified index
		for(int i = 0; i < index; i++)
		{
			current = current.getNext(); 
		}
		
		return current.getElement();
	}

	@Override
	public int indexOf(T element) 
	{
		DLLNode<T> current = head;
		int index = 0;
		boolean found = false;
		
		//search through list until element found or at end of list
		while(!found && current != dummy)
		{
			if(current.getElement() == element)
			{
				found = true; 
			}
			else
			{
				current = current.getNext(); 
				index++; 
			}
		}
		if(!found)
		{
			return -1; 
		}
		
		return index;
	}

	@Override
	public T first() 
	{
		if(isEmpty())
		{
			throw new IllegalStateException("IUDoubleLinkedList - first");
		}
		return head.getElement();
	}

	@Override
	public T last() 
	{
		if(isEmpty())
		{
			throw new IllegalStateException("IUDoubleLinkedList - last");
		}
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) 
	{
		boolean found = false; 
		DLLNode<T> temp = head;
		
		// search list until element found or reach end of list
		while(!found && temp != dummy)
		{
			if(temp.getElement() == target)
			{
				found = true; 
			}
			else
			{
				temp = temp.getNext();
			}
		}
		
		return found;
	}

	@Override
	public boolean isEmpty() 
	{
		return size == 0;
	}

	@Override
	public int size() 
	{
		return size;
	}

	@Override
	public Iterator<T> iterator() 
	{
		return new IUDoubleLinkedListIterator();
	}

	@Override
	public ListIterator<T> listIterator() 
	{
		return new IUDoubleLinkedListIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) 
	{
		if(startingIndex < 0 || startingIndex > size)
		{
			throw new IndexOutOfBoundsException("IUDoubleLinkedList - listIterator(startingIndex)");
		}
		
		return new IUDoubleLinkedListIterator();
	}
	
	
	/**
	 * ListIterator class for IUDoubleLinkedList class 
	 * @author joshuaberkenmeier
	 *
	 */
	private class IUDoubleLinkedListIterator implements ListIterator<T>
	{

		private int itrModCount; 
		private DLLNode<T> next, current, previous; 
		private boolean canModify, didNext, didPrevious; 
		
		/**
		 * default constructor
		 */
		
		public IUDoubleLinkedListIterator()
		{
			itrModCount = modCount; 
			next = head;
			current = null;
			previous = null; 
			canModify = false; 
			didNext = false;
			didPrevious = false; 
		}
		
		/**
		 * checks if modifications were made to outer list
		 * @throws ConcurrentModificationException
		 */
		private void checkModCount()
		{
			if(itrModCount != modCount)
			{
				throw new ConcurrentModificationException("IUDoubleLinkedListIterator - checkModCount"); 
			}
		}
		
		@Override
		public boolean hasNext() 
		{
			return next != dummy;
		}

		@Override
		public T next() 
		{
			checkModCount(); 
			
			if(!hasNext())
			{
				throw new NoSuchElementException("IUDoubleLinkedListIterator - next"); 
			}
			
			T temp = next.getElement();
			
			previous = current; 
			current = next; 
			next = next.getNext(); 
			
			canModify = true;
			didNext = true;
			didPrevious = false;
			
			return temp;
		}

		@Override
		public boolean hasPrevious()
		{
			return next != head;
		}

		@Override
		public T previous() 
		{
			checkModCount(); 
			
			if(!hasPrevious())
			{
				throw new NoSuchElementException("IUDoubleLinkedListIterator - previous"); 
			}
			
			T temp = current.getElement();
			
			next = current;
			current = previous;
			
			if(previous == head || previous == null)
			{
				previous = null;	
			}
			
			else
			{
				previous = previous.getPrevious();
			}
			
			canModify = true;
			didPrevious = true;
			didNext = false;
			
			return temp;
		}

		@Override
		public int nextIndex() 
		{
			if(hasNext())
			{
				return indexOf(next.getElement());
			}
			else
			{
				return size;
			}
		}

		@Override
		public int previousIndex() 
		{
			if(hasPrevious())
			{
				return indexOf(current.getElement());
			}
			else
			{
				return -1;
			}
		}

		@Override
		public void remove() 
		{
			checkModCount();
			
			if(!canModify)
			{
				throw new IllegalStateException("IUDbouleLinkedListIterator - remove");
			}
			
			if(didNext)
			{
				if(current == head)
				{
					next.setPrevious(null);
					current.setNext(null);
					current = null;
					
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
					if(current == tail)
					{
						tail = previous; 
					}
					
					previous.setNext(next);
					next.setPrevious(previous); 
					current.setPrevious(null);
					current.setNext(null); 
					
					if(previous == head)
					{
						current = previous;
						previous = null;
					}
					
					else
					{
						current = previous;
						previous = previous.getPrevious();
					}	
				}
				
				didNext = false;
			}
			
			else if(didPrevious)
			{
				if(next == head)
				{
					if(size == 1)
					{
						next.setNext(null);
						dummy.setPrevious(null);
						next = dummy;
						head = dummy;
						tail = dummy;
					}
					
					else
					{
						DLLNode<T> temp = next.getNext();
						next.setNext(null);
						temp.setPrevious(null);
						next = temp;
						head = temp;
					}
				}
				
				else if(next == tail)
				{
					if(size == 1)
					{
						dummy.setPrevious(null);
						next.setNext(null);
						next = dummy;
						head = dummy;
						tail = dummy;
					}
					
					else
					{
						dummy.setPrevious(current);
						current.setNext(dummy); 
						next.setNext(null);
						next.setPrevious(null);
						next = dummy;
						tail = current;
					}
				}
				
				else
				{
					DLLNode<T> temp = next.getNext();
					current.setNext(temp);
					temp.setPrevious(current);
					next.setNext(null);
					next.setPrevious(null);
					next = temp;
				}
				
				
				didPrevious = false;
			}
			
			canModify = false;
			size--;
			
		}

		@Override
		public void set(T e) 
		{
			checkModCount();
			
			if(!canModify)
			{
				throw new IllegalStateException("IUDbouleLinkedListIterator - set");
			}
			
			if(didNext)
			{
				current.setElement(e);
			}
			
			else if(didPrevious)
			{
				next.setElement(e);
			}
		}

		@Override
		public void add(T e) 
		{
			checkModCount();
			
			DLLNode<T> add = new DLLNode<T>(e);
			
			if(size == 0)
			{
				add.setNext(dummy);
				dummy.setPrevious(add);
				head = add;
				tail = add;
				current = add;
			}
			
			else
			{
				if(current == tail)
				{
					tail = add;
				}
				
				if(current == null)
				{
					add.setNext(next);
					next.setPrevious(add);
					current = add;
					head = add;
				}
				else
				{
					add.setNext(next);
					add.setPrevious(current);
					next.setPrevious(add);
					current.setNext(add);
					previous = current;
					current = add;
				}
			}
			
			size++;
			canModify = false;
		}
		
	}

}
