import java.util.Random;
/**
 * This class builds a hashtable as an array of hashobjects. Has capabilities to insert as linear probe or a double hash.
 * Other methods help to perform the insertions and various needed getters and setters.
 * @author joshuaberkenmeier
 *
 * @param <T>
 */
public class HashTable<T> {
	
	private HashObject<T>[] hashTable;
	private int n, m, m2, numProbes, duplicates;
	private double a;
	
	/**
	 * Constructor: Builds hashtable, initializes size(n), capacity(m), m2(for doublehash),
	 * number of probes and duplicates, takes in a double value for the load factor, and creates an
	 * array at determined capacity filling every spot with null.
	 * @param load
	 */
	public HashTable(double load) 
	{
		n = 0;
		m = getPrime();
		//m = 95791;
		m2 = m - 2;
		numProbes = 0;
		duplicates = 0;
		a = load;
		hashTable = new HashObject[m];
		
		//fill table full of nulls
		for(int i = 0; i < m; i++) 
		{
			hashTable[i] = null;
		}
			
	}
	
	/**
	 * This method will insert hashobjects into the hashtable using linear probing
	 * @param o
	 */
	public void insertLinear(HashObject o) 
	{
		int index = 0;
		int i = 0;
		
		//get index from key value using linear probe hash function
		index = ((o.getKey() % m) + i) % m;
		
		//check to see if spot is empty, if so insert object, increment size and numprobes
		if(hashTable[index] == null) 
		{
			hashTable[index] = o;
			numProbes++;
			o.setProbe(o.getProbe()+1);
			n++;	
		}
		
		//if first spot is not empty, check to see if there are duplicates of the object to insert, if so increment duplicate
		else if(search(o)) 
		{
			duplicates++;			
		}
		
		//if first spot empty, and no duplicates
		else 
		{
			//keep getting new index from hash function as long as spot is occupied, increment numprobes, and i counter
			while(hashTable[index] != null) 
			{	
				numProbes++;
				o.setProbe(o.getProbe()+1);
				i++;  
				index = ((o.getKey() % m) + i) % m;
			}
			
			//when empty spot found, insert object, increment size and numprobes
			hashTable[index] = o;
			numProbes++;
			o.setProbe(o.getProbe()+1);
			n++;
		}
	}

	/**
	 * This method will insert hashobjects into the hashtable using double hashing
	 * @param o
	 */
	public void insertDouble(HashObject o) 
	{
		int index = 0;
		int i = 0;
		
		//get first index with key value using double hashing function
		index = ((o.getKey() % m) + (i * (1 + (o.getKey() % m2)))) % m;
			
		//check to see if spot is empty, if so insert object, increment size and numprobes
		if(hashTable[index] == null) 
		{
			hashTable[index] = o;
			numProbes++;
			o.setProbe(o.getProbe()+1);
			n++;
				
		}
		
		//if first spot is not empty, check to see if there are duplicates of the object to insert, if so increment duplicate
		else if(doubleSearch(o)) 
		{
			duplicates++;			
		}
		
		//if first spot empty, and no duplicates
		else 
		{
			//keep getting new index from hash function as long as spot is occupied, increment numprobes, and i counter
			while(hashTable[index] != null) 
			{			
				numProbes++;
				o.setProbe(o.getProbe()+1);
				i++;
				index = ((o.getKey() % m) + (i * (1 + (o.getKey() % m2)))) % m;
			}
				
			//when empty spot found, insert object, increment size and numprobes
			hashTable[index] = o;
			numProbes++;
			o.setProbe(o.getProbe()+1);
			n++;
		}
	}
	
	/**
	 * This method will randomly select a prime value between 95500-96000 that has a 
	 * twin such as m2 = m -2, where m is the original prime value
	 * @return
	 */
	public int getPrime() 
	{
		Random rand = new Random();
		
		int prime = 0;
		int choice = rand.nextInt(4);
		
		if(choice == 0) 
		{
			prime = 95089;
		}
		
		if(choice == 1) 
		{
			prime = 95791;
		}
		
		if(choice == 2) 
		{
			prime = 95803;
		}
		
		if(choice == 3) 
		{
			prime = 95989;
		}
		
		return prime;
	}

	/**
	 * Get capacity of table
	 * @return int - m
	 */
	public int getCapacity() 
	{
		return m;
	}

	/**
	 * Set capacity of table, and m2 accordingly
	 * @param m
	 */
	public void setCapacity(int m) 
	{
		this.m = m;
		this.m2 = m - 2;
	}

	/**
	 * Get load factor
	 * @return double - a(load)
	 */
	public double getLoad() 
	{
		return a;
	}

	/**
	 * Set load factor
	 * @param a
	 */
	public void setLoad(double a) 
	{
		this.a = a;
	}

	/**
	 * Get size of table (amount of object within table)
	 * @return int - n(size)
	 */
	public int getSize() 
	{
		return n;
	}
	
	/**
	 * Get object within table at specified index
	 * @param i
	 * @return HashObject - hashTable[i]
	 */
	public HashObject getObject(int i) 
	{
		return hashTable[i];
	}

	/**
	 * Get the number of probes
	 * @return int - numProbes
	 */
	public int getNumProbes() 
	{
		return numProbes;
	}
	
	/**
	 * Get duplicates count
	 * @return int - duplicates
	 */
	public int getDuplicates() 
	{
		return duplicates;
	}
	
	/**
	 * Gets the average number of probes after inserting data
	 * @return double - numProbes/n
	 */
	public double getAvgProbes() 
	{
		return (double)numProbes/(double)n;
	}
	
	/**
	 * Search method which goes through the hash table looking for duplicates of the specified object
	 * @param o
	 * @return boolean
	 */
	public boolean search(HashObject o) 
	{
		boolean search = false;
		int i = 0;
		
		//perform search while object hasnt been found or until every spot in table has been checked.
		while(!search && i < m) 
		{
			int index = ((o.getKey() % m) + i) % m;
			
			//skip check if spot in table is null
			if(hashTable[index] == null) 
			{
				break;
			}
			//if object within table equals the specified object, set object duplicate count and switch search to true
			else if(hashTable[index].equals(o)) 
			{	
				hashTable[index].setDuplicate(hashTable[index].getDuplicate() +1);
				search = true;
			}
			
			i++;
		}
		
		return search;
	}
	
	/**
	 * Searches the double hashtable
	 * @param o
	 * @return boolean
	 */
	public boolean doubleSearch(HashObject o) 
	{
		boolean search = false;
		int i = 0;
		
		//perform search while object hasnt been found or until every spot in table has been checked.
		while(!search && i < m) 
		{
			int index = ((o.getKey() % m) + (i * (1 + (o.getKey() % m2)))) % m;
			
			//skip check if spot in table is null
			if(hashTable[index] == null) 
			{
				break;
			}
			
			//if object within table equals the specified object, set object duplicate count and switch search to true
			else if(hashTable[index].equals(o)) 
			{
				hashTable[index].setDuplicate(hashTable[index].getDuplicate() +1);
				search = true;
			}
			
			i++;
		}
		
		return search;
	}
	
	/**
	 * Method to erase the contents of the hash table and reset size and all counters(duplicates/numProbes).
	 */
	public void erase() 
	{
		for(int i = 0; i < m; i++) 
		{
			hashTable[i] = null;
		}
		
		n = 0;
		duplicates = 0;
		numProbes = 0;
	}

}
