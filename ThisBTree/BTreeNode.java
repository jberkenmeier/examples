
/**
 * BTreeNode objects are added to BTree's. Each BTreeNode consists of an array of BTreeElement objects and an array of long values (pointers to child nodes).
 * The other variables used are necessary for proper functioning of BTree insertion and traversal.
 * 
 */
public class BTreeNode
{
    public BTreeElement elements[]; //Using "elements" to avoid confusion with "keys" being both the 64 bit number AND the element in a BTree.
    public long childPointers[];

    private long address;
    private long parentPointer;
    private int size;
    private boolean leaf;

    /**
     * Default constructor for BTreeNode objects. Variables for the BTreeNode are initialized.
     *
     */
    public BTreeNode()
    {
        this.address = -1; //Set address to -1 before written to disk
        this.parentPointer = parentPointer;
        this.childPointers= new long[168 + 1]; //Needs to be 1 larger than the max degree to allow for splitting
        this.elements = new BTreeElement[168];
    }

    /**
     * Sets the pointer value at 'index' in child point array to specified child node address.
     *
     * @param	{long} childAddress
     * @param   {int} index
     *
     */
    public void setChildPointer(long childAddress, int index) {
        this.childPointers[index] = childAddress;
    }

    /**
     * This method returns the size of the BTreeNode.
     *
     * @return	{int} size
     *
     */
    public int getSize()
    {
        return size;
    }

    /**
     * This method sets the size of the BTreeNode to the specified value.
     *
     * @param	size
     *
     */
    public void setSize(int size)
    {
        this.size = size;
    }

    /**
     * Returns true if the BTreeNode it was called upon is a leaf, otherise returns false.
     *
     * @return	{boolean} leaf
     *
     */
    public boolean isLeaf()
    {
        return leaf;
    }

    /**
     * Sets the value for the 'leaf' variable representing whether a BTreeNode is a leaf.
     *
     * @param	{boolean} leaf
     *
     */
    public void setLeaf(boolean leaf)
    {
        this.leaf = leaf;
    }

    /**
     * Checks if address is -1 (meaning the BTreeNode contains no information) before returning the value of the 'address' variable.
     *
     * @return	{long} address
     *
     */
    public long getAddress()
    {
        if(address == -1)
        {
            throw new IllegalArgumentException("The address has not been set. This can occur when attempting to read the address of a node not yet written to disk");
        }
        return address;
    }

    /**
     * Sets the 'address' variable of a BTreeNode.
     * 
     * @param	{long} address
     * 
     */
    public void setAddress(long address)
    {
        this.address = address;
    }

    /**
     * Returns the long value representing the address of a BTreeNode's parent's location within their associated binary file.
     *
     * @return	{long} parentPointer
     *
     */
    public long getParentPointer()
    {
        return parentPointer;
    }

    /**
     * Sets the parentPointer variable for BTreeNode objects.
     *
     * @param	{long} parentPointer
     *
     */
    public void setParentPointer(long parentPointer)
    {
        this.parentPointer = parentPointer;
    }
}

/**
 * This is an internal class for BTreeNode objects, which require a reference to a BTreeElement as part of their constructor.
 * BTreeElements consist of a long value and the number of duplicates of that value. 
 *
 */
class BTreeElement
{
    public long key;
    public long numDuplicates;

    /**
     * The constructor for BTreeElements needs a long value, used as a key in BTreeNodes. The number of duplicates is initially
     * set to zero, but are increased if the same long value is attempted to be inserted again.
     *
     * @param	{long} key
     *
     */
    public BTreeElement(long key)
    {
        this.key = key;
        this.numDuplicates = 0;
    }

    /**
     * This method increases the variable for the number of duplicates.
     *
     */
    public void incrementDuplicates()
    {
        this.numDuplicates++;
    }

    
    /**
     * Converts a long value to its subsequence character representation.
     * 
     * @param	{int} subSequenceLength
     * @param	{long} input
     * @return	{String} String equivalent value of long input
     */
    public String convertToString(int subSequenceLength, long input) {

	int[] binaryArray = new int[2*subSequenceLength]; // there are two binary numbers for each character in a subSequence
	reduceLong(binaryArray.length - 1, binaryArray, input); 
	String retval = "";
	for (int i = 0; i < binaryArray.length-1; i += 2) {
	//for (int i = binaryArray.length - 1; i > 0; i -= 2) { // 2 characters are analyzed and converted to a char at a time
		char newAddChar = '\0';
		if (binaryArray[i] == 0 && binaryArray[i+1] == 0) // 00 is 'a'    
			newAddChar = 'a';
		if (binaryArray[i] == 0 && binaryArray[i+1] == 1) // 01 is 'c'
			newAddChar = 'c';
		if (binaryArray[i] == 1 && binaryArray[i+1] == 0) // 10 is 't'
			newAddChar = 'g';
		if (binaryArray[i] == 1 && binaryArray[i+1] == 1) // 11 is 'g'
			newAddChar = 't';
		retval += newAddChar;
        }
	return retval;
		
    }
	
    /**
     * Recursively divides input by 2, each time checking if an index in binaryArray needs to be 1.
     * 
     * @param	{int} addAtIndex
     * @param	{int[]} binaryArray
     * @param 	{long} input
     * @return	{boolean} arbitrary value returned
     */
    private static boolean reduceLong(int addAtIndex, int[] binaryArray, long input) {
	
 	if (input == 0) 
		return true; // if a program gets here, recursion stops and the program goes back to convertToString
	else if (input == 1 || input % 2 == 1) 
		binaryArray[addAtIndex] = 1;
	reduceLong(addAtIndex - 1, binaryArray, input/2);
	return false; // the program should never get to this point 
	
    }
}
