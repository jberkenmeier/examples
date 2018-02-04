import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail, Josh Berkenmeier
 */
public class CircuitTracer {
	private CircuitBoard board;
	private Storage<TraceState> stateStore;
	private ArrayList<TraceState> bestPaths;

	/** launch the program
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			printUsage();
			System.exit(1);
		}
		try {
			new CircuitTracer(args); //create this with args
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private static void printUsage() {
		//TODO: print out clear usage instructions when there are problems with
		// any command line args
		
		System.out.println("Usage: -s(stack) or -q(queue), -c(print to console) or -g(not implemented), file name");
		
		
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 * @throws FileNotFoundException 
	 */
	private CircuitTracer(String[] args) throws FileNotFoundException {
		
		//TODO: parse command line args
		//TODO: initialize the Storage to use either a stack or queue
		//TODO: read in the CircuitBoard from the given file
		//TODO: run the search for best paths
		//TODO: output results to console or GUI, according to specified choice
		
		//check if first arg is not s or q, print statement
		if(!args[0].equals("-s") && !args[0].equals("-q"))
		{
			printUsage();
			System.out.println("First argument invalid. Please input either -s(stack) or -q(queue)" + "\n");
		}
		
		//check is second arg is not c or g, print statement
		if(!args[1].equals("-c") && !args[1].equals("-g"))
		{
			printUsage();
			System.out.println("Second argument invalid. Please input either -c(console) or -g(gui)" + "\n");
		}
		
		//check if first arg is s, create stack storage
		if(args[0].equals("-s"))
		{
			stateStore = new Storage<TraceState>(Storage.DataStructure.stack);
		}
		
		//check if first arg is q, create queue storage
		if(args[0].equals("-q"))
		{
			stateStore = new Storage<TraceState>(Storage.DataStructure.queue);
		}
		
		//create circuit board from file input in command line
		try
		{
			board = new CircuitBoard(args[2]);
		}
		catch(FileNotFoundException e)
		{
			printUsage();
			System.out.println("Third argument invalid: File not found. Please input a valid file name.");
		}
		
		//initialize arraylist for best paths
		bestPaths = new ArrayList<TraceState>();
		
		//check to see if north position is available from starting point, if so store path in statestore
		if(board.isOpen(board.getStartingPoint().x, board.getStartingPoint().y-1))
		{
			TraceState nPath = new TraceState(board, board.getStartingPoint().x, board.getStartingPoint().y-1); 
			stateStore.store(nPath);
		}
		
		//check to see if south position is available from starting point, if so store path in statestore
		if(board.isOpen(board.getStartingPoint().x, board.getStartingPoint().y+1))
		{
			TraceState sPath = new TraceState(board, board.getStartingPoint().x, board.getStartingPoint().y+1); 
			stateStore.store(sPath);
		}
		
		//check to see if east position is available from starting point, if so store path in statestore
		if(board.isOpen(board.getStartingPoint().x+1, board.getStartingPoint().y))
		{
			TraceState ePath = new TraceState(board, board.getStartingPoint().x+1, board.getStartingPoint().y); 
			stateStore.store(ePath);
		}
		
		//check to see if west position is available from starting point, if so store path in statestore
		if(board.isOpen(board.getStartingPoint().x-1, board.getStartingPoint().y))
		{
			TraceState wPath = new TraceState(board, board.getStartingPoint().x-1, board.getStartingPoint().y); 
			stateStore.store(wPath);
		}
		
		
		while(!stateStore.isEmpty())
		{
			//retrieve trace path from statestore
			TraceState ts = stateStore.retreive();
			
			//check if trace path is complete (at end)
			if(ts.isComplete())
			{
				//if collection of best paths empty, at trace path
				if(bestPaths.isEmpty())
				{
					bestPaths.add(ts);
				}
				
				//if trace path's length equals that of best path found, add to best paths
				else if(ts.pathLength() == bestPaths.get(0).pathLength())
				{
					bestPaths.add(ts);
				}
				
				//if trace path's length is less than that of best path found, clear best paths and add trace path
				else if(ts.pathLength() < bestPaths.get(0).pathLength())
				{
					bestPaths.clear();
					bestPaths.add(ts);
				}
			}
			
			//if trace path is not complete, continue search for open paths and save the resulting trace path to statestore
			else
			{
				//check to see if north position is available from last trace state, if so store path in statestore
				if(ts.isOpen(ts.getRow(), ts.getCol()-1))
				{
					TraceState nPath = new TraceState(ts, ts.getRow(), ts.getCol()-1);
					stateStore.store(nPath);
				}
				
				//check to see if south position is available from last trace state, if so store path in statestore
				if(ts.isOpen(ts.getRow(), ts.getCol()+1))
				{
					TraceState sPath = new TraceState(ts, ts.getRow(), ts.getCol()+1);
					stateStore.store(sPath);
				}
				
				//check to see if east position is available from last trace state, if so store path in statestore
				if(ts.isOpen(ts.getRow()+1, ts.getCol()))
				{
					TraceState ePath = new TraceState(ts, ts.getRow()+1, ts.getCol());
					stateStore.store(ePath);
				}
				
				//check to see if west position is available from starting point, if so store path in statestore
				if(ts.isOpen(ts.getRow()-1, ts.getCol()))
				{
					TraceState wPath = new TraceState(ts, ts.getRow()-1, ts.getCol());
					stateStore.store(wPath);
				}
			}
		}
		
		//check if second arg is c
		if(args[1].equals("-c"))
		{
			//check if no valid paths detected, print statement
			if(bestPaths.size() == 0)
			{
				System.out.println("No paths detected");
			}
			
			//if there are valid best paths, print them to console
			else
			{
				for(int i = 0; i <bestPaths.size(); i++)
				{
					System.out.println(bestPaths.get(i));
				}				
			}
		}
		
		//check if second arg is g
		if(args[1].equals("-g"))
		{
			System.out.println("GUI output not implemented. Results displayed to console.");
			
			//check if no valid paths detected, print statement
			if(bestPaths.size() == 0)
			{
				System.out.println("No paths detected");
			}
			
			//if there are valid best paths, print them to console
			for(int i = 0; i <bestPaths.size(); i++)
			{
				System.out.println(bestPaths.get(i));
			}
		}
	}
} // class CircuitTracer
