import java.util.Random;

/**
 * This class Generates a new process for scheduling
 * 
 * @author jberk
 *
 */
public class ProcessGenerator {
	
	private double probability;
	Random random;
	
	/**
	 * Constructor: takes a double for the probability as a parameter
	 * @param prob
	 */
	public ProcessGenerator(double prob) 
	{
		probability = prob;
		
	}
	
	/**
	 * Checks to see if the random value is less than the probability
	 * @return
	 */
	public boolean query() 
	{
		Boolean check = false;
		random = new Random();
		
		if(random.nextDouble() < probability) 
		{
			check = true;
		}
		
		return check;
	}
	
	/**
	 * Creates a new process
	 * @param currentTime
	 * @param maxProcTime
	 * @param maxPriorityLvl
	 * @return
	 */
	public Process getNewProcess(int currentTime, int maxProcTime, int maxPriorityLvl ) 
	{
		
		int priorityLvl = random.nextInt(maxPriorityLvl + 1);
		int timeFinish = random.nextInt(maxProcTime + 1);
		
		Process process = new Process(currentTime, priorityLvl, timeFinish, maxPriorityLvl);
		
		return process;
	}
	
	/**
	 * Sets the probability
	 * @param set
	 */
	private void setProbability(double set) 
	{
		probability = set;
	}
	
	
	

}
