import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * FileParser objects are responsible are parsing a file for DNA input data and converting each subsequnce of the specified 
 * length to long values. Each long value is inserted into the BTree being constructed by using the BTree reference
 * passed into this class' constructor.
 *
 * @author Devan
 */
public class FileParser {

	private String filename;
	private int subSequenceLength;

	// a circular character array is used for the cache
	private char[] charCache;
	private int cacheAddIndex;
	private int cacheSize;
	private int cacheSubSequenceStartIndex;

	private BTree btree;

	/**
	 * Constructor for FileParser objects. Variables are set and the character cache array is initialized.
	 *
	 * @param filename
	 * @param subSequenceLength
	 * @throws IOException
	 */
	public FileParser(String filename, int subSequenceLength, BTree btree) throws IOException {
		this.btree = btree;
		this.filename = filename;
		this.subSequenceLength = subSequenceLength;
		charCache = new char[60]; // characters are added
	}

	/**
	 * Scanner objects are used to look for DNA sequence data. The 'activelyParsing' is turned on when 'ORIGIN' is found 
	 * is turned off when '//' is found. This state is turned on and off at least once (assuming an ORIGIN has been found)
	 * but can also be turned on and off many more times.  
	 *
	 * @throws IOException
	 *
	 */
	public void parseFile() throws IOException {
		File file = new File(filename);
		Scanner fileScan = new Scanner(file);
		boolean activelyParsing = false;
		int numAA = 0;
		while (fileScan.hasNext()){
			String currentLine = fileScan.nextLine().toLowerCase();
			Scanner lineScan = new Scanner(currentLine);
			while (lineScan.hasNext()) {
				String currentWord = lineScan.next();
				if (currentWord.equals("origin")) {
					activelyParsing = true;
					break;
				}
			}
			while (activelyParsing) {
				currentLine = fileScan.nextLine().toLowerCase();
				lineScan = new Scanner(currentLine);
				while (lineScan.hasNext()) {
					String currentWord = lineScan.next();
					if (currentWord.equals("//")) {
						//System.out.println("\\\\ was detected");
						activelyParsing = false;
						cacheSubSequenceStartIndex = cacheAddIndex;
						cacheSize = 0;
						break;
					}
					else if (isntNum(currentWord)){
						for (int i = 0; i < currentWord.length(); i++) {
							if (currentWord.charAt(i) == 'n') {
								//System.out.println("'n' was detected");
								cacheSubSequenceStartIndex = cacheAddIndex;
								cacheSize = 0;
								//break;
							}
							else{
								updateCacheIndices();
								charCache[cacheAddIndex] = currentWord.charAt(i);
								if (cacheSize < charCache.length)
									cacheSize++;
								cacheAddIndex++;
								if (cacheSize >= subSequenceLength) {
									updateCacheIndices();
									String testSequence = fetchSubSequence();
									long newLong = convertToLong(testSequence);
									btree.InsertKey(new BTreeElement(newLong));
								}
							}
						}
					}
				}
			}
		}
		fileScan.close();
		long x = btree.getNumDuplicates(btree.getRoot(), new BTreeElement(convertToLong("tt")));
	}

	/**
	 * A circular character array is used for the character cache. Variables that point to indices in the array
	 * need to be reset to zero when they reach the end.
	 *
	 */
	private void updateCacheIndices() {
		if (cacheAddIndex == charCache.length)
			cacheAddIndex = 0;
		if (cacheSubSequenceStartIndex == charCache.length)
			cacheSubSequenceStartIndex = 0;
	}

	/**
	 * Analyzes current state of cache character array and builds a String by adding the amount of characters specified by 'subSequenceLength'.
	 *
	 * @return	{String} subSequence
	 *
	 */
	private String fetchSubSequence() {
		String subSequence = "";
		updateCacheIndices();
		for (int index = cacheSubSequenceStartIndex, count = 0; count < subSequenceLength; count++, index++) {
			if (index == charCache.length)
				index = 0;
			subSequence += charCache[index];
		}
		cacheSize--;
		cacheSubSequenceStartIndex++;
		return subSequence;
	}

	/**
	 * This method converts a String consisting of 'a', 'c', 't', and/or 'g' to its 64-bit encoded value according
	 * to the value discussed in the project write-up.
	 *
	 * @param	{String} input 
	 * @return 	{long} 64-bit encoded value
	 *
	 */
	 public static long convertToLong(String input) {
		long subSequenceConverted = 0;
		input = input.toLowerCase();
		for (int i = input.length()-1, j = 0; i >= 0; i--, j+=2) {
			if (input.charAt(i) == 'a') {
				subSequenceConverted += 0;
			}
			else if (input.charAt(i) == 't') {
				subSequenceConverted += (long)Math.pow(2, j);
				subSequenceConverted += (long)Math.pow(2, j+1);
			}
			else if (input.charAt(i) == 'c') {
				subSequenceConverted += (long)Math.pow(2, j);
			}
			else if (input.charAt(i) == 'g') {
				subSequenceConverted += (long)Math.pow(2, j+1);
			}
		}
		return subSequenceConverted;
	}
	/**
	 /** This method checks to see if the String passed in is a number.
	 *
	 * @param 	{String} input
	 * @return	{boolean} true if 'input' isn't a number, else false
	 *
	 */
	private boolean isntNum(String input) {
		boolean retval = false;
		try {
			int test = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			retval = true;
		}
		return retval;
	}

	/**
	 * Before other functions are called on a FileParser object, we confirm that the provided
	 * file contains an "origin" indicating the presence of DNA sequence data.
	 *
	 * @return	{boolean} true if ORIGIN is found, else false 
	 * @throws 	FileNotFoundException
	 *
	 */
	public boolean containsOrigin() throws FileNotFoundException {
		File file = new File(filename);
		Scanner fileScan = new Scanner(file);
		while (fileScan.hasNext()){
			String currentLine = fileScan.nextLine().toLowerCase();
			Scanner lineScan = new Scanner(currentLine);
			while (lineScan.hasNext()) {
				String currentWord = lineScan.next();
				if (currentWord.equals("origin")) {
					return true;
				}
			}
		}
		return false;
	}

}