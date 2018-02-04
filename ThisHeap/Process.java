/**
 * Creates a new process for priority queue
 * @author jberk
 *
 */
public class Process {
	
	private int priorityLevel, timeToFinish, timeNotProcessed, arrivalTime, maxPriorityLevel;

	/**
	 * Constructor
	 * @param arrival
	 * @param priority
	 * @param finish
	 * @param maxPriority
	 */
	public Process(int arrival, int priority, int finish, int maxPriority) 
	{
		arrivalTime = arrival;
		priorityLevel = priority;
		timeToFinish = finish;
		maxPriorityLevel = maxPriority;
	}
	
	/**
	 * Reduces the time remaining for the process
	 */
	public void reduceTimeRemaining() 
	{
		if(timeToFinish > 0) 
		{
			timeToFinish--;			
		}
	}
	
	/**
	 * Increments the time not processed
	 */
	public void incrementTimeNotProcessed() 
	{
		timeNotProcessed++;
	}
	
	/**
	 * Gets the time not processed
	 * @return int
	 */
	public int getTimeNotProcessed() 
	{
		return timeNotProcessed;
	}
	
	/**
	 * Gets the time remaining for process
	 * @return int
	 */
	public int getTimeRemaining() 
	{
		return timeToFinish;
	}
	
	/**
	 * Checks to see if the process is done
	 * @return
	 */
	public boolean done() 
	{
		return timeToFinish == 0;
	}
	
	/**
	 * Gets the arrival time of the process
	 * @return int
	 */
	public int getArrivalTime() 
	{
		return arrivalTime;
	}
	
	/**
	 * Gets the priority of the process
	 * @return int
	 */
	public int getPriority() 
	{
		return priorityLevel;
	}
	
	/**
	 * Increments the priority of the process by 1
	 */
	public void incrementPriority() 
	{
		if(priorityLevel < maxPriorityLevel) 
		{
			priorityLevel++;
		}
	}
	
	/**
	 * Resets the time not processes to 0
	 */
	public void resetTimeNotProcessed() 
	{
		timeNotProcessed = 0;
	}
	
	/**
	 * Sets the priority level to the specified amount
	 * @param priority
	 */
	private void setPriorityLevel(int priority) 
	{
		priorityLevel = priority;
	}
	
	/**
	 * Gets the time to finish for the process
	 * @return int
	 */
	public int getTimeToFinish() 
	{
		return timeToFinish;
	}
	
	/**
	 * Sets the time to finish for the process
	 * @param set
	 */
	private void setTimeToFinish(int set) 
	{
		timeToFinish = set;
	}
	
	/**
	 * Sets the time not processes
	 * @param set
	 */
	private void setTimeNotProcessed(int set) 
	{
		timeNotProcessed = set;
	}
	
	/**
	 * Sets the arrival time of the process
	 * @param set
	 */
	private void setArrivalTime(int set) 
	{
		arrivalTime = set;
	}
	
	/**
	 * Gets the max priority level of the process
	 * @return
	 */
	public int getMaxPriorityLevel() 
	{
		return maxPriorityLevel;
	}
	
	/**
	 * Sets the max priority level of the process
	 * @param set
	 */
	public void setMaxPriorityLevel(int set) 
	{
		maxPriorityLevel = set;
		
		if(maxPriorityLevel < priorityLevel) 
		{
			priorityLevel = maxPriorityLevel;
		}
	}
	
}
