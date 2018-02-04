import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Represents a 2D circuit board as read from an input file.
 *  
 * @author mvail, Josh Berkenmeier
 */
public class CircuitBoard {
	private char[][] board;
	private Point startingPoint;
	private Point endingPoint;

	//constants you may find useful
	private final int ROWS; //initialized in constructor
	private final int COLS; //initialized in constructor
	private final char OPEN = 'O'; //capital 'o'
	private final char CLOSED = 'X';
	private final char TRACE = 'T';
	private final char START = '1';
	private final char END = '2';
	private final String ALLOWED_CHARS = "OXT12";

	/** Construct a CircuitBoard from a given board input file, where the first
	 * line contains the number of rows and columns as ints and each subsequent
	 * line is one row of characters representing the contents of that position.
	 * Valid characters are as follows:
	 *  'O' an open position
	 *  'X' an occupied, unavailable position
	 *  '1' first of two components needing to be connected
	 *  '2' second of two components needing to be connected
	 *  'T' is not expected in input files - represents part of the trace
	 *   connecting components 1 and 2 in the solution
	 * 
	 * @param filename
	 * 		file containing a grid of characters
	 * @throws FileNotFoundException if Scanner cannot read the file
	 * @throws InvalidFileFormatException for any other format or content issue that prevents reading a valid input file
	 */
	public CircuitBoard(String filename) throws FileNotFoundException {
		
		
		Scanner fileScan = new Scanner(new File(filename));
		
		//TODO: parse the given file to populate the char[][]
		// throw FileNotFoundException if Scanner cannot read the file
		// throw InvalidFileFormatException if any formatting or parsing issues are encountered
		
		try
		{
			ROWS = Integer.parseInt(fileScan.next());//parses the first two values in the text files
			COLS = Integer.parseInt(fileScan.next());//to create a grid with the right dimensions
		}
		catch(NumberFormatException e)
		{
			throw new InvalidFileFormatException("Dimension not a integer");
		}
		
		checkFormat(filename);
		
		//initialize board with parsed dimensions
		board = new char[ROWS][COLS];
		fileScan.nextLine();
		
		int i = 0;
		int oneCounter = 0;
		int twoCounter = 0;
		
		//algorithm to parse elements in file and input them into a circuit board
		while(fileScan.hasNextLine())
		{
			String line = fileScan.nextLine();
			Scanner linescan = new Scanner(line);
			
			while(linescan.hasNext())	//puts all the values from the text file into a basegrid
			{							
				for(int j = 0 ; j < board[i].length; j++)
			    {
					char c = linescan.next().charAt(0);//turn elements into char so it can be input into board	
					
					//if element is 1, initialize starting point at elements location
					if(c == START)
					{
						startingPoint = new Point(i,j);
						oneCounter++;
					}
					
					//if element is 2, initialize ending point at elements location
					if(c == END)
					{
						endingPoint = new Point(i,j);
						twoCounter++;
					}
					
					//check to see if there are more than one starting or ending point
					if(oneCounter > 1 || twoCounter > 1)
					{
						throw new InvalidFileFormatException("More than one starting or endpoint");
					}
					
					//check if there are invalid characters in input file
					if(c != START && c != END && c != OPEN && c != CLOSED)
					{
						throw new InvalidFileFormatException("Character in file that is not 'O', 'X', '1', or '2'");
					}
					board[i][j] = c;	
					
					
			    }
				
				i++;
			}
			
			linescan.close();
		}
		fileScan.close();
		
		//check if there is no starting or ending point
		if(oneCounter == 0 || twoCounter == 0)
		{
			throw new InvalidFileFormatException("File is missing a starting or endpoint");
		}
		
	}
	
	/** Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	/** utility method for copy constructor
	 * @return copy of board array */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}
	
	/** Return the char at board position x,y
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}
	
	/** Return whether given board position is open
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open 
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}
	
	/** Set given position to be a 'T'
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}
	
	/** @return starting Point */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}
	
	/** @return ending Point */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}
	
	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}
	
	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}
	
	/**
	 * Format checker. Throws exception if file format is not correct
	 * @param file
	 * @throws FileNotFoundException
	 */
	public static void checkFormat(String file) throws FileNotFoundException
	{
	
			int numLines = 0;//to keep track of number of lines(rows)
			int numCol = 0;//to keep track of number of columns

			File f = new File(file);
			Scanner scan = new Scanner(f);//create scanner to read file
				
			String line = scan.nextLine();//grab a string representation of first line
			Scanner linescan = new Scanner(line);//create scanner to read the first line
				
			int row = linescan.nextInt();//grab first dimension integer, throws exception if not integer
			int col = linescan.nextInt();//grab second dimension integer, throws exception if not integer
			
				
			if (linescan.hasNext())//check first line for another dimension after the first two, throws exception if true
			{
				throw new InvalidFileFormatException("More than one dimension");
			}
				
			while(scan.hasNextLine())//while the scanner can find a next line, do the following
			{
				String l = scan.nextLine();//read in nextLine
				StringTokenizer token = new StringTokenizer(l);//break up scanned line into tokens
				numCol = token.countTokens(); //count tokens of line
				numLines ++;//increment number of lines count(number of rows)
					
				Scanner scn = new Scanner(l);//create scanner to read individual lines
					
				if(numCol != col)//check if number of columns equals declared number of columns, throw exception if they dont match
				{
					throw new InvalidFileFormatException("Number of columns dont match declared column dimension");
				}
					
			}
				
			if(numLines != row)//checks if counted number of line(rows) equals the declared number of rows, throws exception if not
			{
				throw new InvalidFileFormatException("Number of rows dont match declared row dimension");
			}	
	}
	
}// class CircuitBoard
