/**
 * This class is the driver class for the parent and sub-classes of type Clock. Puts all of the time pieces into a 
 * bag data structure. Runs off different time intervals for each clock(Day, week, month, year) and displays
 * each clock after the time interval. 
 * @author joshuaberkenmeier
 *
 */
public class ClockSimulation {
	
	private static Bag<Clock> bag;//declares bag variable
	private static final long ONE_DAY = 86400;//final variable for amount of seconds in a day
	private static final long ONE_WEEK = 604800;//final variable for amount of seconds in a week
	private static final long ONE_MONTH = 2592000;//final variable for amount of seconds in a month
	private static final long ONE_YEAR = 31536000;//final variable for amount of seconds in a year

	/**
	 * Main driver method, does all of the work on the clocks and displays their time.
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
		bag = new Bag<Clock>();//creates the bag
		
		bag.add(new Sundial());//add the sundial timepiece to the bag
		bag.add(new CuckooClock());//add the cuckoo clock timepiece to the bag
		bag.add(new GrandfatherClock());//add the grandfather clock timepiece to the bag
		bag.add(new AtomicClock());//add the atomic clock timepiece to the bag
		bag.add(new WristWatch());//add the wrist watch timepiece to the bag
		
		System.out.println("Times before start:");//print statement for start time	
		display();//displays the time of each time piece
		System.out.println();//print a separation line
		
		System.out.println("After one day:");//print statement for after one day increment
		
		for(int i = 0; i < ONE_DAY; i++)//for loop to call tick method for amount of seconds in a day
		{
			tick();//tick method takes each clock out of the bag and ticks off one second
		}
		display();//takes each clock out of the bag and calls their unique display methods
		reset();//reset each clocks time and drift back to midnight
		System.out.println();//print a separation line
		
		System.out.println("After one week:");//print statement for after one week increment
		for(int i = 0; i < ONE_WEEK; i++ )//for loop to call tick method for amount of seconds in a week
		{
			tick();//tick method takes each clock out of the bag and ticks off one second
		}
		display();//takes each clock out of the bag and calls their unique display methods
		reset();//reset each clocks time and drift back to midnight
		System.out.println();//print a separation line
		
		System.out.println("After one month:");//print statement for after one month increment
		for(int i = 0; i < ONE_MONTH; i++)//for loop to call tick method for amount of seconds in a month
		{
			tick();//tick method takes each clock out of the bag and ticks off one second
		}
		display();//takes each clock out of the bag and calls their unique display methods
		reset();//reset each clocks time and drift back to midnight
		System.out.println();//print a separation line
		
		System.out.println("After one year:");//print statement for after one year increment
		for(int i = 0; i < ONE_YEAR; i++)//for loop to call tick method for amount of seconds in a month
		{
			tick();//tick method takes each clock out of the bag and ticks off one second
		}
		display();//takes each clock out of the bag and calls their unique display methods
		  
	}
	
	/**
	 * Static method that takes each timepiece out of the bag and increments their time by one second
	 */
	private static void tick()
	{
		for(int i = 0; i < bag.getSize(); i++)//for loop to iterate through all of the elements in the bag
		{
			bag.get(i).tick();//tick a second off of each timepiece in bag
		}
	}
	
	/**
	 * Static method that takes each timepiece out of the bag and calls their unique display methods
	 */
	private static void display()
	{
		for(int i = 0; i < bag.getSize(); i++)//for loop to iterate through all of the elements in the bag
		{
			bag.get(i).display();//calls display method for each timepiece in bag
		}
	}
	
	/**
	 * Static method that takes each timepiece out of the bag and resets their time and drift back to midnight/zero
	 */
	private static void reset()
	{
		for(int i = 0; i < bag.getSize(); i++)//for loop to iterate through all of the elements in the bag
		{
			bag.get(i).reset();//resets each timepiece back to midnight/zero
		}
	}

}