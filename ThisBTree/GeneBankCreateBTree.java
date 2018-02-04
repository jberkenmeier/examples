import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Scanner;

/**
 * This is the driver class which facilitates the creation of BTree objects based off of command line arguments entered.
 *
 * @author: Joshua, Caleb, and Devan
 *
 */
public class GeneBankCreateBTree {

    BTreeNode root;
    int m;
    int t;

    /**
     * This is the main method, the program starts here when creating BTrees.
     * @throws IOException
     *
     */
    public static void main (String[] args) throws InterruptedException, IOException {

	// command line argument reference
    	// args[0] is the cache --> fails if not 0 because not implemented
        // args[1] is the degree --> fails if less than 2
        // args[2] is the gbk file --> fails if it doesn't exist or doesn't contain origin
        // args[3] is the sequence length --> fails if it's < 1 or > 31
        // (optional) args[4] is the debug level --> fails if != 0 and != 1

    	//check if no arguments
    	if(args.length == 0) {
    		printUsage();
    		throw new IllegalArgumentException("No arguments were entered");
    	}
    	
    	// checking format of args[0]  
    	if((Integer.parseInt(args[0])) != 0) {
    		
    	 printUsage();
   		 throw new IllegalArgumentException("No cache implemented");
    	}
    	
    	// checking format of args[1]..degree
        try {
            int check = Integer.parseInt(args[1]);
        } catch(NumberFormatException e){
            System.err.println("args[1] is not an integer");
            printUsage();
        }
        // checking format of args[3]..sequence length
        try{
           int check = Integer.parseInt(args[3]);
        } catch(NumberFormatException g){
           System.err.println("args[3] is not an integer");
           printUsage();
        }
        // checking value of args[1]
        if (Integer.parseInt(args[1]) == 1 || Integer.parseInt(args[1]) < 0){
        	printUsage();
        	throw new IllegalArgumentException("The degree must be greater than or equal to 2 or 0.");
        }
        // checking value of args[3]
        else if (Integer.parseInt(args[3]) < 1 || Integer.parseInt(args[3]) > 31){
           printUsage();
           throw new IllegalArgumentException("The sequence length is an integer that must be between 1 and 31 (inclusive).");
        }
        // checking value of args[3] if four arguments were entered
        else if (args.length == 5 && (Integer.parseInt(args[4]) != 0 && Integer.parseInt(args[4]) != 1)){
           printUsage();
           throw new IllegalArgumentException("Debug level must be 0 or 1.");
        }
        else {
            try {
                File file = new File(args[2]);
                Scanner scan = new Scanner(file);
                scan.close();
                BTree bTree = new BTree(args[2] + ".btree.data." + args[3] + "." + (args[1] == "0" ? "88" : args[1]), Integer.parseInt(args[1]));
                FileParser parser = new FileParser(args[2], Integer.parseInt(args[3]), bTree);
                if (parser.containsOrigin()){ // this check is necessary because one of the test files does not contain an ORIGIN
                   parser.parseFile();
                } else {
                    System.err.println("'" + args[2] + "' does not contain an ORIGIN - BTree was not created");
                    printUsage();
                }
                
                //if argument for debug is entered, and that value is 1
                if(args.length == 5 && Integer.parseInt(args[4]) == 1) 
                {
                	
                	try {
                		
                		PrintStream out = new PrintStream(new FileOutputStream("dump"));
                		//sun.misc.Queue<BTreeElement> queue = new sun.misc.Queue<>();
                		ArrayDeque<BTreeElement> listOfElements;
                		
                		//Iterate through b-tree
                		BTreeNode root = bTree.getRoot();
                		listOfElements = bTree.iterateBTree(root);
                		
                		//grab every element/sequence, get duplicate count
                		while(!listOfElements.isEmpty()) 
                		{
                			BTreeElement current = listOfElements.remove();
                			String seq = current.convertToString(Integer.parseInt(args[3]), current.key);
                			out.println("Frequency: " + (current.numDuplicates + 1) + "\t" + "Sequence: " + seq);//need to convert key back to sequence                			
                		}
                		
                		out.close();
                		
                	} catch (FileNotFoundException e) {
                		e.printStackTrace();
                	}
                }
                
                
            } catch (FileNotFoundException f) {
                System.out.println("file not found: " + args[2]);
                printUsage();
            }

        }
    }

    /**
     * Prints usage message specified in project write-up. This method is called when incorrect input is detected.
     *
     */
    public static void printUsage(){
        System.out.println("java GeneBankCreateBTree <0, no cache> <degree> <gbk file> <sequence length> [<debug level>]");
    }

}