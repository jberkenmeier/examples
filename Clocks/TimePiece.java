/**
 * Interface required for all classes of type Clock, requires them to have a tick, display, and reset method.
 * @author joshuaberkenmeier
 *
 */

public interface TimePiece {
	/**
	 * resets the TimePiece to the starting time (00:00:00 - midnight by default)
	 */
	public void reset();

	/**
	 * Simulates one second of time passing
	 */
	public void tick();
	
	/**
	 * Displays the current time of the TimePiece
	 */
	public void display();
}
