/**
 * This class builds a simple HashObject that holds values for object, key, duplicates, and probes.
 * @author joshuaberkenmeier
 *
 * @param <T>
 */
public class HashObject<T> {
	
	private T object;
	private int duplicate, probe, key;
	
/**
 * Constructor: Initializes object, key, probe, and duplicate variables.
 * @param object
 */
	public HashObject(T object)
	{
		this.object = object;
		key = Math.abs(object.hashCode());
		probe = 0;
		duplicate = 0;
	}
	
	/**
	 * Get key value
	 * @return int - key
	 */
	public int getKey() 
	{
		return key;
	}
	
	/**
	 * Set key value
	 * @param key
	 */
	public void setKey(int key) 
	{
		this.key = key;
	}

	/**
	 * Get object
	 * @return T - object
	 */
	public T getObject() 
	{
		return object;
	}

	/**
	 * Set object
	 * @param object
	 */
	public void setObject(T object) 
	{
		this.object = object;
	}
	
	/**
	 * Get duplicate count
	 * @return int - duplicate
	 */
	public int getDuplicate() 
	{
		return duplicate;
	}

	/**
	 * Set duplicate 
	 * @param duplicate
	 */
	public void setDuplicate(int duplicate) 
	{
		this.duplicate = duplicate;
	}

	/**
	 * Get probe count
	 * @return int - probe
	 */
	public int getProbe() 
	{
		return probe;
	}

	/**
	 * Set probe
	 * @param probe
	 */
	public void setProbe(int probe) 
	{
		this.probe = probe;
	}

	/**
	 * Equals method, overwrites equals(). Checks for key values
	 * @param o
	 * @return boolean - equal
	 */
	public boolean equals(HashObject o) 
	{
		Boolean equal = false;
		
		if(o.getKey() == this.getKey()) 
		{
				equal = true;
		}
		
		return equal;
	}
	
	/**
	 * toString method, overwrites toString(), prints out object, duplicate count, probe count
	 * @return string - str
	 */
	public String toString() 
	{
		String str = (String) this.getObject() + " " + this.getDuplicate() + " " + this.getProbe();
		return str;
	}

}

