import java.io.*;

/**
 * This class is run after a BTree is constructed and BTreeNodes were inserted. Searches for subsequences are conducted
 * and the relevant information is printed out.
 *
 */
public class GeneBankSearch
{
    private static BufferedReader queryFile;
    private static RandomAccessFile btreeFile;
    private static int debugLevel;
    private static BTree bTree;
    
    /**
     * This is the main method for GeneBankSearch, the program starts here and checks the number of arguments. If the correct
     * number of arguments were entered, which is three, the argument are interpretd on an individual level, the BTree object 
     * being searched through is instantiated, and the query file is parsed.
     *
     */
    public static void main(String args[]) throws IOException
    {
        if (args.length == 0) // just in case
	{ 
	     printUsage();
	     throw new IllegalArgumentException("No arguments were entered.");
	}
	if(Integer.parseInt(args[0]) != 0) // everything except 1 is accepted 
	{ 
	     printUsage();
	     throw new IllegalArgumentException("No cache implemented.");
        }
	if (!(args.length == 3 || args.length == 4)) 
	{   // three arguments -> 0 for cache, btree file, query file entered
	    // four arguments -> 0 for cache, btree file, query file, debug level entered
            printUsage();
            throw new IllegalArgumentException("Wrong number of program arguments.");
        }
        readArgs(args);
        bTree = new BTree(btreeFile);
        readQueryFile();
    }

    /**
     * This method reads through each line of a query files and puts together information about the number of occurences of 
     * each subsquence being searched for.
     * 
     */
    private static void readQueryFile() throws IOException
    {
        String nextLine;

        while((nextLine = queryFile.readLine()) != null)
        {
            long key = FileParser.convertToLong(nextLine);
            long numOccurrences = bTree.getNumDuplicates(bTree.getRoot(), new BTreeElement(key));
            numOccurrences = numOccurrences == -1 ? 0 : numOccurrences + 1;
            System.out.println("Num occurrences of " + nextLine + ": " + numOccurrences);
        }
    }

    /**
     * This method accepts the command line arguments entered for a search and initializes the variables
     * used for a search.
     *
     * @param	{String} args[]
     *
     */
    private static void readArgs(String args[])
    {
	try
        {
            btreeFile = new RandomAccessFile(args[1], "r"); // changed 0 to 1
        }
        catch (FileNotFoundException e)
        {
            //System.err.println("BTree file not found");
            System.err.println("Query file not found");
            System.exit(1);
        }

        try
        {
            queryFile = new BufferedReader(new FileReader(args[2])); // changed 1 to 2
        }
        catch (FileNotFoundException e)
        {
            //System.err.println("Query file not found");
            System.err.println("Query file not found");
            System.exit(1);
        }
    }

    /**
     * This method prints the usage message for running a gene bank search. 
     *
     */
    private static void printUsage()
    {
        System.err.println("GeneBankSearch <btree file> <query file> [<debug level>]");
    }
}