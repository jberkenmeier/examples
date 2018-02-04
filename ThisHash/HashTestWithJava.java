import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;

/**
 * This class is the main driver for the hashtable program. Creates a hash table and inserts different
 * values depending on user input (1 = random number, 2 = current time, 3 = word-list) using both
 * linear probing and double hashing, retaining all stats from each, and printing stats to the console and, if
 * user specified, to a dump file.
 * @author joshuaberkenmeier
 *
 */
public class HashTestWithJava {
	private static String dataType;
	private static int totalDup = 0;
	private static int intSize, floatSize, strSize;

	public static void main(String[] args) 
	{
		//check if more than 3 arguments were input
		if(args.length > 4) 
		{
			System.out.println("Usage: java HashTest <input type> <load factor> <tableSize> [<debug level>]");
			System.exit(0);
			
		}
		
		//if argument one(input type) is not 1, 2, or 3
		if(Integer.parseInt(args[0]) != 1 && Integer.parseInt(args[0]) != 2 && Integer.parseInt(args[0]) != 3 ) 
		{
			System.out.println("Usage: input type should be either 1 for ints, 2 for longs, or 3 for words");
			System.exit(0);
		}
		
		//if argument two(load factor) is 1 or greater.
		if(Double.parseDouble(args[1]) >= 1.0) 
		{
			System.out.println("Usage: load factor should be less than 1");
			System.exit(0);
		}
		
		
		
		int tableSize = Integer.parseInt(args[2]);
		float load = Float.parseFloat(args[1]);
		
		
		//if input type is one(random number)
		if(Integer.parseInt(args[0]) == 1) 
		{
			dataType = "Random number generator";
			Hashtable<Integer, Integer> hashTable = new Hashtable<Integer, Integer>(Integer.parseInt(args[2]));
			Random linearInteger = new Random(1234);
			
			int object = linearInteger.nextInt();
			int intSize = 0;
			
			while(intSize < ((double)tableSize * load)) 
			{
				if(hashTable.get(object) != null) 
				{
					int dup = hashTable.get(object);
					dup++;
					totalDup++;
					hashTable.remove(object);
					hashTable.put(object, dup);
				}
				else 
				{
					hashTable.put(object,0);
					intSize++;
				}
			}
			
			//if user wants to print output to dump file
			if(args.length == 4 && Integer.parseInt(args[3]) == 1) 
			{
				try {
				      PrintStream out = new PrintStream(new FileOutputStream("hashmap-dump"));
				    
				      out.println(hashTable.toString());

				      out.close();

				    } catch (FileNotFoundException e) {
				      e.printStackTrace();
				    }
			}
			
		}
		
		//if input type is two(current time)
		if(Integer.parseInt(args[0]) == 2) 
		{
			dataType = "Current system time";
			Hashtable<Float, Integer> hashTable = new Hashtable<Float, Integer>(Integer.parseInt(args[2]));
			
			float object = System.currentTimeMillis();
			floatSize = 0;
			//while load factor has yet to be reached, insert float values
			while(floatSize < ((double)tableSize * load)) 
			{
				if(hashTable.get(object) != null) 
				{
					int dup = hashTable.get(object);
					dup++;
					totalDup++;
					hashTable.remove(object);
					hashTable.put(object, dup);
				}
				else 
				{
					hashTable.put(object,0);
					floatSize++;
				}
			}
			
			//if user wants to print output to dump file
			if(args.length == 4 && Integer.parseInt(args[3]) == 1) 
			{
				try {
				      PrintStream out = new PrintStream(new FileOutputStream("hashmap-dump"));
				     
				     out.println(hashTable.toString()); 
				      
				      out.close();

				    } catch (FileNotFoundException e) {
				      e.printStackTrace();
				    }
			}
			
			
		}
		
		if(Integer.parseInt(args[0]) == 3) 
		{
			dataType = "Word-list";
			Hashtable<String, Integer> hashTable = new Hashtable<String, Integer>(Integer.parseInt(args[2]));
			
			File f = new File("word-list");

			strSize = 0;
			
			try
			{
				Scanner filescan = new Scanner(f);
				
				//while load factor has yet to be reached, insert values using linear probing
				while(strSize < ((double)tableSize * load)) 
				{	
					String object = null;
					String line = filescan.nextLine();
					Scanner linescan = new Scanner(line);
						
					if(linescan.hasNext()) 
					{
						object = linescan.next();
						
						if(hashTable.get(object) != null) 
						{
							int dup = hashTable.get(object);
							dup++;
							totalDup++;
							hashTable.remove(object);
							hashTable.put(object, dup);
						}
						else 
						{
							hashTable.put(object,0);
							strSize++;
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
			
			//if user wants to print output to dump file
			if(args.length == 4 && Integer.parseInt(args[3]) == 1) 
			{
				try {
				      PrintStream out = new PrintStream(new FileOutputStream("hashmap-dump"));

				      out.close();

				    } catch (FileNotFoundException e) {
				      e.printStackTrace();
				    }
			}
			
		}
		
		//print results to console
	
			DecimalFormat df = new DecimalFormat("0.000");
					
			System.out.println("Table size: " + tableSize);
			System.out.println("Data source type: " + dataType);
			
			System.out.println("Using JAVA Hashtable...");
			
			if(Integer.parseInt(args[0]) == 1) 
			{
				System.out.println("Inserted " + intSize + " elements, of which " + totalDup + " duplicates");
			}
			
			if(Integer.parseInt(args[0]) == 2) 
			{
				System.out.println("Inserted " + floatSize + " elements, of which " + totalDup + " duplicates");
			}
			
			if(Integer.parseInt(args[0]) == 3) 
			{
				System.out.println("Inserted " + strSize + " elements, of which " + totalDup + " duplicates");
			}
			
			System.out.println("Load factor = " + load);

	}

}

