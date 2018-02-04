import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * This is the driver class for the Cache program. It handle arguments from the 
 * command line and creates either 1 or 2 cache objects with specified sizes from user input.
 * It then reads in the user input file name to a scanner and parses through the strings of the
 * text file. It then uses Cache methods to load the strings into the cache object(s) and runs
 * stats such as hit/accesses/hit rate.
 * 
 * 
 * @author joshuaberkenmeier
 *
 */
public class Test {
	
	private static Cache<String> cache1, cache2;
	private static int size1, size2;
	
	public static void main(String[] args) throws Exception 
	{
		
		//if agruments are less or more than 3 or 4, print statement and exit program
		if(args.length < 3 || args.length > 4) 
		{
			System.out.println("Arguments need to be either 3 or 4 in length.");
			System.out.println("java Test [1 | 2] [L1 cache size] [ | L2 cache size] [filename]");
			System.exit(1);
		}
		
		size1 = Integer.parseInt(args[1]);
		
		cache1 = new Cache<String>(size1);
		
		//if first argument 1, creates a one-level cache, runs the text through, and prints stats
		if(args[0].equals("1")) 
		{		
			String f = args[2];
			File file = new File(f);
			
			Scanner filescan = new Scanner(file);
			StringTokenizer dataGrab;
			
			String line;
			String word;
			
			while(filescan.hasNextLine()) 
			{
				line = filescan.nextLine();
				dataGrab = new StringTokenizer(line, " ");
				
				while(dataGrab.hasMoreTokens()) 
				{
					word = dataGrab.nextToken();
					cache1.get(word);
					
				}
			}
			
			DecimalFormat decFmt = new DecimalFormat("0.00");
			
			System.out.println("L1 cache with " + size1 + " entries created.");
			System.out.println("...");
			System.out.println("Number of L1 cache hits: " + cache1.getcacheHits());
			System.out.println("L1 cache hit rate: " + decFmt.format(cache1.getHitRate() * 100) + "%\n" );
			System.out.println("Total number of accesses: " + cache1.getcacheAccesses());
			System.out.println("Total number of cache hits: " + cache1.getcacheHits());
			System.out.println("Overall hit rate: " + decFmt.format(cache1.getHitRate() * 100) + "%");
			
			
		}
		
		//if first argument 2, creates a two-level cache, runs the text through and prints out stats.

		if(args[0].equals("2")) 
		{
			
			size2 = Integer.parseInt(args[2]);
			cache2 = new Cache<String>(size2);
			
			String f = args[3];
			File file = new File(f);
			
			Scanner filescan = new Scanner(file);
			StringTokenizer dataGrab;
			
			String line;
			String word;
			
			while(filescan.hasNextLine()) 
			{
				line = filescan.nextLine();
				dataGrab = new StringTokenizer(line, " ");
				
				while(dataGrab.hasMoreTokens()) 
				{
					word = dataGrab.nextToken();
					
					if(cache1.get(word) != null) 
					{
						cache2.remove(word);
						cache2.add(word);
					}
					
					else 
					{
						cache2.get(word);
					}
					
				}
			}
			
			
			DecimalFormat decFmt = new DecimalFormat("0.00");
			
			int totalHits = cache1.getcacheHits() + cache2.getcacheHits();
			double overallHitRate = ((double)cache1.getcacheHits() + (double)cache2.getcacheHits())/ (double)cache1.getcacheAccesses();
			
			System.out.println("L1 cache with " + size1 + " entries created.");
			System.out.println("L2 cache with " + size2 + " entries created.");
			
			System.out.println("...");
			
			System.out.println("Number of L1 cache hits: " + cache1.getcacheHits());
			System.out.println("L1 cache hit rate: " + decFmt.format(cache1.getHitRate() * 100) + "%\n" );
			
			System.out.println("Number of L2 cache hits: " + cache2.getcacheHits());
			System.out.println("L2 cache hit rate: " + decFmt.format(cache2.getHitRate() * 100) + "%\n" );
			
			System.out.println("Total number of accesses: " + cache1.getcacheAccesses());
			System.out.println("Total number of cache hits: " + totalHits);
			System.out.println("Overall hit rate: " + decFmt.format(overallHitRate * 100) + "%");
			

		}
		
	}
}

