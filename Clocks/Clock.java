/**
 * This is a abstract parent class, which implements TimePiece interface. Has an enumeration for different
 * types of clocks, get and set methods for clock type, and the required methods from TimePiece interface.
 * @author joshuaberkenmeier
 *
 */

public abstract class Clock implements TimePiece{
	
	private ClockType clockType;
	protected Time time;
	
	/**
	 * 
	 * enumeration that holds all the different clock types
	 *
	 */
	public enum ClockType
	{
		natural, mechanical, digital, quantum	
	}
	
	/**
	 * Constructor, takes a clock type, and drift amount
	 * @param clockType - ClockType
	 * @param driftAmt - double
	 */
	public Clock (ClockType clockType, Double driftAmt)
	{
		this.clockType = clockType;
		time = new Time(0, 0, 0, driftAmt);
	}

	/**
	 * Method to retrieve clock type
	 * @return clockType
	 */
	public ClockType getClockType() 
	{
		return clockType;
	}

	/**
	 * Method to set clock type
	 * @param clockType
	 */
	public void setClockType(ClockType clockType) 
	{
		this.clockType = clockType;
	}
	
	/**
	 * Method to reset the time and drift amount of your clock
	 */
	public void reset()
	{
		time.resetToStartTime();
	}
	
	/**
	 * Method to tick of one second from your clock
	 */
	public void tick()
	{
		time.incrementTime();
	}	
	
	/**
	 * Abstract display method, required from interface
	 */
	public abstract void display();
	
}
