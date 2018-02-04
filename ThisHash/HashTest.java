import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
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
public class HashTest {
	private static HashTable<HashObject> hashTable;
	private static String dataType;
	private static double linearProbes, doubleProbes;
	private static int linearDup, doubleDup;

	public static void main(String[] args) 
	{
		//check if more than 3 arguments were input
		if(args.length > 3) 
		{
			System.out.println("Usage: java HashTest <input type> <load factor> [<debug level>]");
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
		
		//if input type is one(random number)
		if(Integer.parseInt(args[0]) == 1) 
		{
			dataType = "Random number generator";
			hashTable = new HashTable<HashObject>(Double.parseDouble(args[1]));
			Random linearInteger = new Random(1234);
			Random doubleInteger = new Random(1234);

			HashObject<Integer> o;
			
			//while load factor has yet to be reached, insert values using linear probing
			while(hashTable.getSize() < ((double)hashTable.getCapacity() * hashTable.getLoad())) 
			{
				hashTable.insertLinear(o = new HashObject<Integer>(linearInteger.nextInt()));
			}
			
			//if user wants to print output to dump file
			if(args.length == 3 && Integer.parseInt(args[2]) == 1) 
			{
				try {
				      PrintStream out = new PrintStream(new FileOutputStream("linear-dump"));
				      for (int i = 0; i < hashTable.getCapacity(); i++) 
				      {
				    	  if(hashTable.getObject(i) != null) 
				    	  {
				    		  out.println("Table[" + i + "]: " + hashTable.getObject(i).toString());
				    	  }
				    	  
				      }

				      out.close();

				    } catch (FileNotFoundException e) {
				      e.printStackTrace();
				    }
			}
			
			//capture numprobes and numduplicates for linear probing, erase table
			linearProbes = hashTable.getAvgProbes();
			linearDup = hashTable.getDuplicates();
			hashTable.erase();
			
			//while new table has yet to reach load factor, insert values using double hashing
			while(hashTable.getSize() < ((double)hashTable.getCapacity() * hashTable.getLoad())) 
			{
				hashTable.insertDouble(o = new HashObject<Integer>(doubleInteger.nextInt()));
			}
			
			//if user wants to print output to dump file
			if(args.length == 3 && Integer.parseInt(args[2]) == 1) 
			{
				try {
				      PrintStream out = new PrintStream(new FileOutputStream("double-dump"));
				      for (int i = 0; i < hashTable.getCapacity(); i++) 
				      {
				    	  if(hashTable.getObject(i) != null) 
				    	  {
				    		  out.println("Table[" + i + "]: " + hashTable.getObject(i).toString());
				    	  }
				    	  
				      }

				      out.close();

				    } catch (FileNotFoundException e) {
				      e.printStackTrace();
				    }
			}
			
			//capture numprobes and numduplicates for double hashing
			doubleProbes = hashTable.getAvgProbes();
			doubleDup = hashTable.getDuplicates();
		}
		
		//if input type is two(current time)
		if(Integer.parseInt(args[0]) == 2) 
		{
			dataType = "Current system time";
			hashTable = new HashTable<HashObject>(Double.parseDouble(args[1]));
			
			HashObject<Long> o;
			
			//while load factor has yet to be reached, insert values using linear probing
			while(hashTable.getSize() < ((double)hashTable.getCapacity() * hashTable.getLoad())) 
			{
				hashTable.insertLinear(o = new HashObject<Long>(System.currentTimeMillis()));
			}
			
			//if user wants to print output to dump file
			if(args.length == 3 && Integer.parseInt(args[2]) == 1) 
			{
				try {
				      PrintStream out = new PrintStream(new FileOutputStream("linear-dump"));
				      for (int i = 0; i < hashTable.getCapacity(); i++) 
				      {
				    	  if(hashTable.getObject(i) != null) 
				    	  {
				    		  out.println("Table[" + i + "]: " + hashTable.getObject(i).toString());
				    	  }
				    	  
				      }

				      out.close();

				    } catch (FileNotFoundException e) {
				      e.printStackTrace();
				    }
			}
			
			//capture numprobes and numduplicates for linear probing, erase table
			linearProbes = hashTable.getAvgProbes();
			linearDup = hashTable.getDuplicates();
			hashTable.erase();
			
			//while new table has yet to reach load factor, insert values using double hashing
			while(hashTable.getSize() < ((double)hashTable.getCapacity() * hashTable.getLoad())) 
			{
				hashTable.insertDouble(o = new HashObject<Long>(System.currentTimeMillis()));
				
			}
			
			//if user wants to print output to dump file
			if(args.length == 3 && Integer.parseInt(args[2]) == 1) 
			{
				try {
				      PrintStream out = new PrintStream(new FileOutputStream("double-dump"));
				      for (int i = 0; i < hashTable.getCapacity(); i++) 
				      {
				    	  if(hashTable.getObject(i) != null) 
				    	  {
				    		  out.println("Table[" + i + "]: " + hashTable.getObject(i).toString());
				    	  }
				    	  
				      }

				      out.close();

				    } catch (FileNotFoundException e) {
				      e.printStackTrace();
				    }
			}
			
			//capture numprobes and numduplicates for double hashing
			doubleProbes = hashTable.getAvgProbes();
			doubleDup = hashTable.getDuplicates();
		}
		
		if(Integer.parseInt(args[0]) == 3) 
		{
			dataType = "Word-list";
			hashTable = new HashTable<HashObject>(Double.parseDouble(args[1]));
			
			File f = new File("word-list");

			HashObject<String> o;
			
			try
			{
				Scanner filescan = new Scanner(f);
				
				//while load factor has yet to be reached, insert values using linear probing
				while(hashTable.getSize() < ((double)hashTable.getCapacity() * hashTable.getLoad())) 
				{	
					String line = filescan.nextLine();
					Scanner linescan = new Scanner(line);
						
					if(linescan.hasNext()) 
					{
						
						hashTable.insertLinear(o = new HashObject<String>(linescan.next()));
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
			if(args.length == 3 && Integer.parseInt(args[2]) == 1) 
			{
				try {
				      PrintStream out = new PrintStream(new FileOutputStream("linear-dump"));
				      for (int i = 0; i < hashTable.getCapacity(); i++) 
				      {
				    	  if(hashTable.getObject(i) != null) 
				    	  {
				    		  out.println("Table[" + i + "]: " + hashTable.getObject(i).toString());
				    	  }
				    	  
				      }

				      out.close();

				    } catch (FileNotFoundException e) {
				      e.printStackTrace();
				    }
			}
			
			//capture numprobes and numduplicates for linear probing, erase table
			linearProbes = hashTable.getAvgProbes();
			linearDup = hashTable.getDuplicates();
			hashTable.erase();
			
			try
			{
				Scanner filescan = new Scanner(f);
				
				//while new table has yet to reach load factor, insert values using double hashing
				while(hashTable.getSize() < ((double)hashTable.getCapacity() * hashTable.getLoad())) 
				{	
					String line = filescan.nextLine();
					Scanner linescan = new Scanner(line);
						
					if(linescan.hasNext()) 
					{
						hashTable.insertDouble(o = new HashObject<String>(linescan.next()));						
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
			if(args.length == 3 && Integer.parseInt(args[2]) == 1) 
			{
				try {
				      PrintStream out = new PrintStream(new FileOutputStream("double-dump"));
				      for (int i = 0; i < hashTable.getCapacity(); i++) 
				      {
				    	  if(hashTable.getObject(i) != null) 
				    	  {
				    		  out.println("Table[" + i + "]: " + hashTable.getObject(i).toString());
				    	  }
				    	  
				      }

				      out.close();

				    } catch (FileNotFoundException e) {
				      e.printStackTrace();
				    }
			}
			
			//capture numprobes and numduplicates for double hashing
			doubleProbes = hashTable.getAvgProbes();
			doubleDup = hashTable.getDuplicates();

		}
		
		//print results to console
		if(args.length == 2 || args.length == 3) 
		{
			DecimalFormat df = new DecimalFormat("0.000");
					
			System.out.println("A good table size is found: " + hashTable.getCapacity());
			System.out.println("Data source type: " + dataType);
			System.out.println();
			
			System.out.println("Using Linear Hashing...");
			System.out.println("Inserted " + hashTable.getSize() + " elements, of which " + linearDup + " duplicates");
			System.out.println("Load factor = " + args[1] + ", Avg. no. of probes " + df.format(linearProbes));
			
			System.out.println("Using Double Hashing...");
			System.out.println("Inserted " + hashTable.getSize() + " elements, of which " + doubleDup + " duplicates");
			System.out.println("Load factor = " + args[1] + ", Avg. no. of probes " + df.format(doubleProbes));
		}

	}

}

