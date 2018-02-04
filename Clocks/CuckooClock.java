import java.text.DecimalFormat;

/**
 * Class for a wristwatch clock. Has a constructor and its own personal display method
 * @author joshuaberkenmeier
 *
 */
public class CuckooClock extends Clock {

	/**
	 * Constructor, calls on parent class constructor which takes a clock type and drift amount
	 */
	public CuckooClock() 
	{
		super(ClockType.mechanical, 0.000694444);
	}

	@Override
	public void display() 
	{
		DecimalFormat decFmt = new DecimalFormat("0.00");//decimal formatter for drift amount
		System.out.println("mechanical cuckoo clock:  time [" + time.formattedTime() + "], total drift = " + decFmt.format(time.getTotalDrift()) );
	}

}
