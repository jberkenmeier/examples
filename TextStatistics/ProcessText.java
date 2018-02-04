import java.io.File;
/**
 * 		   This class depends on user input of text files to the console.
 * 		   Processes the user files and returns a variety of statistics.
 * @author jberkenm
 *
 */
public class ProcessText {
/**
 * 		  Main method: Takes the user args (textfiles) and makes a TextStatistics
 * 					   object (which scans through the files collecting various
 * 					   statistics), and used the modified toString method to print
 * 					   the statistics of each file to the console. 
 * @param args
 */
	public static void main(String[] args) {
		
		if (args.length == 0)
		{
			System.err.println("Usage: java ProcessText file1 [file2 ...]");
			System.exit(1);
		}
		
		for(int i = 0; i < args.length; i++)
		{
			File file = new File(args[i]);
			if (!file.exists())
			{
				System.err.println("Invalid filepath: " + file);
			}
			else
			{
				TextStatistics text = new TextStatistics(file);
				System.out.println(text.toString());
				
			}
		}
	
	}

}
