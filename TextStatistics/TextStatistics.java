import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
/**
 * 		   This class takes a text file for a parameter 
 * 		   and parses through lines of that text to 
 * 		   collect and return statistics.
 *  
 * @author Josh Berkenmeier
 *
 */


public class TextStatistics implements TextStatisticsInterface {
	
	private File file;
	private int charCount;
	private int wordCount;
	private int lineCount;
	private int[] letterCount;
	private int[] wordLength;
	private double avgLength;
	private static final String DELIMITERS = "[\\W\\d_]+";
	
	/**
	 * 
	 * Constructor: Takes a textfile as parameter, uses static methods
	 * 				to parse through lines of text in order to 
	 * 				collects statistics on the textfile.
	 */
	public TextStatistics(File file)
	{
		charCount = charCount(file);
		lineCount = lineCount(file);
		wordCount = wordCount(file);
		wordLength = wordLengthCount(file);
		avgLength = avgWordLength(file);
		letterCount = letterCount(file);
		this.file = file;
		
		
	}

	/**
	 * @return the number of characters in the text file
	 */
	public int getCharCount() 
	{
		return charCount;
	}

	/**
	 * @return the number of words in the text file
	 */
	public int getWordCount() 
	{	
		return wordCount;
	}

	/**
	 * @return the number of lines in the text file
	 */
	public int getLineCount() 
	{	
		return lineCount;
	}

	/**
	 * @return the letterCount array with locations [0]..[25] for 'a' through 'z'
	 */
	public int[] getLetterCount() 
	{	
		return letterCount;
	}

	/**
	 * @return the wordLengthCount array with locations [0]..[23] with location [i]
	 * storing the number of words of length i in the text file. Location [0] is not used.
	 * Location [23] holds the count of words of length 23 and higher.
	 */
	public int[] getWordLengthCount() 
	{	
		return wordLength;
	}

	/**
	 * @return the average word length in the text file
	 */
	public double getAverageWordLength() 
	{	
		return avgLength;
	}
	/**
	 * 
	 * @param f takes a file as a parameter
	 * @return numChar 	static method that scans a file and returns the amount
	 * 					of characters in that file
	 */
	private static int charCount(File f)
	{
		int numChar = 0;
		
		try
		{
			Scanner filescan = new Scanner(f);
			filescan.useDelimiter(DELIMITERS);
			
			while (filescan.hasNextLine())
			{
				String line = filescan.nextLine();
				 numChar += line.length() + 1;
			}
			
		filescan.close();
		}
		catch(FileNotFoundException errorObject)
		{
			System.out.println("File " + f + " cannot be opened");
			System.exit(1);
		}
		return numChar;
	}
	/**
	 * 
	 * @param f takes a file as a parameter
	 * @return numLines static method that scans a file and returns the amount
	 * 					of lines in that file
	 */
	private static int lineCount(File f)
	{
		int numLines = 0;
		
		try
		{
			Scanner filescan = new Scanner(f);
			filescan.useDelimiter(DELIMITERS);
			
			while(filescan.hasNextLine())
			{
				filescan.nextLine();
				numLines ++;
			}
			
			filescan.close();
		}
		catch(FileNotFoundException errorObject)
		{
			System.out.println("File " + f + " cannot be opened");
			System.exit(1);
		}
		
		return numLines;
	}
	/**
	 * 
	 * @param f takes a file as a parameter
	 * @return numWords static method that scans a file and returns the amount
	 * 					of words in that file
	 */
	private static int wordCount(File f)
	{
		int numWords = 0;
		
		try
		{
			Scanner filescan = new Scanner(f);
			filescan.useDelimiter(DELIMITERS);
			
			while(filescan.hasNextLine())
			{
				String line = filescan.nextLine();
				Scanner linescan = new Scanner(line);
				linescan.useDelimiter(DELIMITERS);
				
				while(linescan.hasNext())
				{
					linescan.next();
					numWords ++;
				}
				linescan.close();
			}
			filescan.close();
		}
		catch(FileNotFoundException errorObject)
		{
			System.out.println("File " + f + " cannot be opened");
			System.exit(1);
		}
		
		return numWords;
		
	}
	/**
	 * 
	 * @param f takes a file as a parameter
 	 * @return wordLengthCount		static method that scans a file and returns an array
 	 *  							that represents the length of words in a file
 	 *  							(up to 23 chars) and the frequency they occur.
	 * 				  
	 */
	private static int[] wordLengthCount(File f)
	{
		int[] wordLengthCount = new int[24];
		
		try
		{
			Scanner filescan = new Scanner(f);
			filescan.useDelimiter(DELIMITERS);
			
			
			while(filescan.hasNextLine())
			{
				String line = filescan.nextLine();
				Scanner linescan = new Scanner(line);
				linescan.useDelimiter(DELIMITERS);
				
				while(linescan.hasNext())
				{
					String word = linescan.next();
					if(word.length() < 23)
					{
						wordLengthCount[word.length()]++;						
					}
					else
					{
						wordLengthCount[23]++;
					}
				}
				linescan.close();
			}
			filescan.close();
		}
		catch(FileNotFoundException errorObject)
		{
			System.out.println("File " + f + " cannot be opened");
			System.exit(1);
		}
		
		return wordLengthCount;
	
	}
	/**
	 * 
	 * @param f takes a file as a parameter
	 * @return avgWordLength 	static method that scans a file and returns the average
	 * 							word length of the file
	 */
	private static double avgWordLength(File f)
	{
		double added = 0.0;
		double total = 0.0;
		double wordLengthCount = 0.0;
		
		try
		{
			Scanner filescan = new Scanner(f);
			filescan.useDelimiter(DELIMITERS);
			
			
			while(filescan.hasNextLine())
			{
				String line = filescan.nextLine();
				Scanner linescan = new Scanner(line);
				linescan.useDelimiter(DELIMITERS);
				
				while(linescan.hasNext())
				{
					String word = linescan.next();
					added += word.length();
					total ++;	
				}
				linescan.close();
			}
			filescan.close();
			
			wordLengthCount = added/total;
			
		}
		catch(FileNotFoundException errorObject)
		{
			System.out.println("File " + f + " cannot be opened");
			System.exit(1);
		}
		
		return wordLengthCount;
	
	}
	/**
	 * 
	 * @param f takes a file as a parameter
	 * @return letters 	static method that scans a file and returns an array
	 * 					representing the alphabet (a-z as index 0-25) and the
	 * 					amount of times they occur in the file
	 */
	private static int[] letterCount(File f)
	{
		int[] letters = new int[26];
		char c;
		
		try
		{
			Scanner filescan = new Scanner(f);
			filescan.useDelimiter(DELIMITERS);
			
			
			while(filescan.hasNextLine())
			{
				String line = filescan.nextLine().toLowerCase();
				
				for (int i = 0; i < line.length(); i++)
				{
					c = line.charAt(i);
					if(c >= 'a' && c <= 'z' )
						letters[c - 'a']++;
				}
			}
			filescan.close();
			
		}
		catch(FileNotFoundException errorObject)
		{
			System.out.println("File " + f + " cannot be opened");
			System.exit(1);
		}
		
		return letters;
	
	}
	/**
	 * 	modified toString method
	 */
	public String toString()
	{
		String str = "";
		str += "Statistics for " + file + "\n";
		str += "==========================================================\n";
		str += lineCount + " lines\n";
		str += wordCount + " words\n";
		str += charCount + " characters\n";
		str += "------------------------------\n";
		str += String.format(" %-15s%-15s\n", "a = " + letterCount[0], "n = " + letterCount[13]);
		str += String.format(" %-15s%-15s\n", "b = " + letterCount[1], "o = " + letterCount[14]);
		str += String.format(" %-15s%-15s\n", "c = " + letterCount[2], "p = " + letterCount[15]);
		str += String.format(" %-15s%-15s\n", "d = " + letterCount[3], "q = " + letterCount[16]);
		str += String.format(" %-15s%-15s\n", "e = " + letterCount[4], "r = " + letterCount[17]);
		str += String.format(" %-15s%-15s\n", "f = " + letterCount[5], "s = " + letterCount[18]);
		str += String.format(" %-15s%-15s\n", "g = " + letterCount[6], "t = " + letterCount[19]);
		str += String.format(" %-15s%-15s\n", "h = " + letterCount[7], "u = " + letterCount[20]);
		str += String.format(" %-15s%-15s\n", "i = " + letterCount[8], "v = " + letterCount[21]);
		str += String.format(" %-15s%-15s\n", "j = " + letterCount[9], "w = " + letterCount[22]);
		str += String.format(" %-15s%-15s\n", "k = " + letterCount[10], "x = " + letterCount[23]);
		str += String.format(" %-15s%-15s\n", "l = " + letterCount[11], "y = " + letterCount[24]);
		str += String.format(" %-15s%-15s\n", "m = " + letterCount[12], "z = " + letterCount[25]);
		str += "------------------------------\n";
		str += " length  frequency\n";
		str += " ------  ---------\n";
		for(int i = 1; i < 24; i++)
		{
			if(wordLength[i] > 0)
			{
				str += String.format(" %6d%11d\n", i, wordLength[i]);
			}
		}
		str += "\n";
		DecimalFormat dcm = new DecimalFormat("0.00");
		str += "Average word length = " + dcm.format(avgLength) + "\n";
		str += "==========================================================";
		
		return str;
	}
	
	
	
	
}
