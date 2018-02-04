import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;


/**
 *
 * This BTree class contains two constructors for BTree instaniations and the
 * necessary methods for inserting long values and reading
 *
 * BTreeNodes from binary files. Long key values are added to BTree objects in
 * the FileParser class.
 */
public class BTree
{
    public final int TREE_MAX_DEGREE = 168;
    public final int NODE_SIZE_BYTES = 4096;
    public final int NODE_ADDRESS_SIZE = 8;
    public final int CHILD_POINTERS_START_OFFSET = 8;
    public final int ELEMENTS_START_OFFSET = 1368;
    public final int NODE_SIZE_OFFSET = 4062;
    public final int IS_LEAF_OFFSET = 4066;

    int totalDuplicates = 0;
    private long rootAddress;
    private long nextOpenLocation = 4096; //First 4096 used for metadata (Removes pointers to 0, which can be confused with a null pointer).
    private long lastWriteLocation = 0;
    private BTreeNode root;
    private RandomAccessFile dataFile;
    private ByteBuffer longBuffer; //Making a class member to avoid constant re-creation
    private int t;
    private int m;

    /**
     *
     * This constructor is used to instantiate BTree objects and make them from
     * scratch, meaning that information is being read in from a DNA file and then
     * inserted into the newly created BTree.
     *
     * @param {String} filename
     * @param {int} t
     *
     */
    public BTree(String filename, int t) throws IOException
    {
        t = (t == 0 ? 84 : t);
        File file = new File(filename);
        System.out.println(file.getAbsolutePath());
        try
        {
            dataFile = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e)
        {
            System.out.println("ERROR! Could not find initial file to create RandomAccessFile");
            System.exit(1);
        }
        this.t = t;
        this.m = t*2;
        longBuffer = ByteBuffer.allocate(Long.BYTES);

        root = new BTreeNode();
        root.setLeaf(true);

        //Read and write node to get address
        rootAddress = nextOpenLocation;
        writeNode(root, true);
        root = readNode(rootAddress);
        setRoot(root);

        dataFile.seek(0);
        dataFile.writeInt(t); //Min degree stored at beginning of file.
    }

    /**
     *
     * This constructor is used to instantiate a BTree object based on an existing
     * binary file containing the information of a BTree.
     * @param {RandomAccessFile} btreeFile
     */
    public BTree(RandomAccessFile btreeFile) throws IOException
    {
       dataFile = btreeFile;
       this.t = dataFile.readInt();
       this.rootAddress = dataFile.readLong();
       this.m = t*2;
       longBuffer = ByteBuffer.allocate(Long.BYTES);

        this.root = readNode(rootAddress);
    }

    /**
     *
     * This method accepts a long value which represents the location of a BTreeNode
     * which is then returned. Part of this process includes
     * handling the conversion process of turning binary information of the desired
     * BTreeNode to variable value for use throughout this class.
     * @param {long} location
     *
     * @return {BTreeNode} node at 'location' index in file
     */
    public BTreeNode readNode(long location) throws IOException
    {
        BTreeNode outputNode = new BTreeNode();

        dataFile.seek(location + NODE_SIZE_OFFSET); //Need to read size info first, this is at the end of the file due to past versions
        outputNode.setSize( dataFile.readInt());
        outputNode.setLeaf(dataFile.readBoolean());
        outputNode.setAddress(location);

        dataFile.seek(location); //Set file pointer to target location
        long parentPointer = dataFile.readLong(); //Read the parent pointer
        outputNode.setParentPointer(parentPointer);

        long childPointer;
        int i = 0;
        while (i < outputNode.getSize() + 1) //Keep getting child pointers and adding them to the array until we reach a terminator
        {
            childPointer = dataFile.readLong();
            outputNode.childPointers[i] = childPointer;
            i++;
        }

        dataFile.seek(location + ELEMENTS_START_OFFSET); //Set file pointer to the beginning of the element list in the node.
        long key;
        i = 0;
        while ((key = dataFile.readLong()) != -1) //Keep reading keys until we reach the terminator
        {
            BTreeElement newElement = new BTreeElement(key);
            newElement.numDuplicates = dataFile.readLong(); //The number of duplicates directly follows the key
            outputNode.elements[i] = newElement;
            i++;
        }


        return outputNode;
    }

    /***
     * Stores a BTReeNode, starting at the given address, in the BTree binary file.
     *
     * Node layout:
     * *****************************************************************************************************************
     * --Total Size: 4096 bytes
     *
     * --Bytes 0-7 [0x-0x7]: Parent Pointer.
     *
     * --Bytes 8-1367 [0x8-0x557](169*8 + 8 byte terminator): Pointers to children, in indexed order. (IE Bytes 8-15 are pointer
     *   to child 0), terminated by a long value of -1.
     *
     * --Bytes 1368-4063 [0x558 - 0xFDF](168 * 16 + 8 byte terminator): BTreeNodes, in indexed order, first key, then num duplicates,
     *   terminated by a long value of -1.
     *
     * --Bytes 4062-4069 [0xFDE-0xFE7]: Size integer and isLeaf boolean.
     *
     * --Bytes 4070-4095 [0xFE7-0xFFF]: Undefined (
     *
     * A long value of -1 (0xFFFFFFFFFFFFFFFF in binary) indicates the end of the values in the BTREE array.
     * *****************************************************************************************************************
     *
     * @param {BTreeNode}  The node to write to disk
     */
    public void writeNode(BTreeNode node, boolean isNew) throws IOException
    {
        long location;
        if(isNew)
        {
            location = nextOpenLocation;
            nextOpenLocation += NODE_SIZE_BYTES;
        }
        else
        {
            location = node.getAddress();
        }

        if (this.dataFile.length() < location + NODE_SIZE_BYTES) //If file isn't long enough to hold an entire node, increase its size.
        {
            this.dataFile.setLength(location + NODE_SIZE_BYTES); //Incrementing by a single node size each time may not be ideal (Depends on how expensive increasing the length is)
        }

        this.dataFile.seek(location); //Set the file-pointer to the correct location
        this.dataFile.writeLong(node.getParentPointer()); //Write the parent pointer first

        for (int i = 0; i < node.childPointers.length; i++) //Write each child pointer in order
        {
            if (node.childPointers[i] == -1) //Stop writing once we reach the terminator
            {
                break;
            }
            this.dataFile.writeLong(node.childPointers[i]);

        }
        this.dataFile.writeLong(-1); //After the last child, write a negative 1 terminator.

        this.dataFile.seek(location + (node.childPointers.length + 2) * NODE_ADDRESS_SIZE); //Move file pointer to beginning of BTreeElementArray (Offset is num pointers * pointer size + terminator size + parent pointer size)
        for (int i = 0; i < node.elements.length; i++) //Write each element in order
        {
            if (node.elements[i] == null || node.elements[i].key == -1) //Stop writing once we reach the terminator or a null
            {
                break;
            }
            this.dataFile.writeLong(node.elements[i].key);
            this.dataFile.writeLong(node.elements[i].numDuplicates);
        }
        this.dataFile.writeLong(-1); //After the last child, write a negative 1 terminator.

        this.dataFile.seek(location + NODE_SIZE_OFFSET);
        this.dataFile.writeInt(node.getSize());
        this.dataFile.writeBoolean(node.isLeaf());
        this.dataFile.seek(location + NODE_SIZE_BYTES); //Not currently necessary to move file-pointer to next node position, but it is a clean way to advance the pointer.

        lastWriteLocation = location;
    }

    public BTreeNode insertDuplicates(BTreeNode node, BTreeElement key) throws IOException
    {
        int i = 0;

        while(i < node.getSize() && key.key > node.elements[i].key)
        {
            i++;
        }

        if(i < node.getSize() && key.key == node.elements[i].key)
        {
            node.elements[i].incrementDuplicates();
            return node;
        }

        if(node.isLeaf())
        {
            return null;
        }

        else
        {
            return insertDuplicates(readNode(node.childPointers[i]), key);
        }

    }


    /**
     * Preserving key of type BTreeElement for consistency with insertKey.
     * @param {BTreeNode} node
     * {BTreeElement} key
     *
     * @return {long} number of duplicates
     */
    public long getNumDuplicates(BTreeNode node, BTreeElement key) throws IOException
    {
        int i = 0;

        while(i < node.getSize() && key.key > node.elements[i].key)
        {
            i++;
        }

        if(i < node.getSize() && key.key == node.elements[i].key)
        {
            return node.elements[i].numDuplicates;
        }

        if(node.isLeaf())
        {
            return -1;
        }
        else
        {
            return getNumDuplicates(readNode(node.childPointers[i]), key);
        }
    }

    /**
     *
     * Inserts a BTreeElement into a BTree. BTreeElements are based on the converted
     * long values in the FileParser class.
     *
     * Insertion behavior is different based on whether the root is full.
     *
     * @param {BTreeElement} key
     */
    public void InsertKey(BTreeElement key) throws IOException
    {
        BTreeNode r = this.getRoot();
        BTreeNode nodeWithDuplicate = this.insertDuplicates(this.getRoot(), key);
        if(nodeWithDuplicate != null)
        {
            writeNode(nodeWithDuplicate, false);
            return;
        }

        if(r.getSize() == (2*t-1))
        {
            BTreeNode s = new BTreeNode();
            //Read and write s to get address
            writeNode(s, true);
            s = readNode(lastWriteLocation);
            this.setRoot(s);
            s.setLeaf(false);
            s.setChildPointer(r.getAddress(), 0);
            bTreeSplitChild(s, 0);
            bTreeInsertNonfull(s, key);
        }

        else
        {
            bTreeInsertNonfull(r, key);
        }
    }

    /**
     *
     * This method is called when an element was inserted into a BTree at a node
     * which is now at its max capacity.
     *
     * The node is split into two nodes, each of which have about half of the keys
     * of the original node. Pointers to child nodes and parent nodes are updated.
     *
     * @param {BTreeNode} x
     * @param {integer} i
     */
    private void bTreeSplitChild(BTreeNode x, int i) throws IOException
    {

        BTreeNode z = new BTreeNode();
        BTreeNode y = readNode(x.childPointers[i]);

        if(y.isLeaf())
        {
            z.setLeaf(true);
        }
        else
        {
            z.setLeaf(false);
        }

        z.setSize(t-1); //find out why


        for(int j = 0; j <= t - 2; j ++)
        {
            z.elements[j] = y.elements[j + t];
        }

        if(!y.isLeaf())
        {
            for(int j = 0; j <= t - 1; j++)
            {
                z.childPointers[j] = y.childPointers[j + t];
            }
        }

        y.setSize(t - 1);  //figure out why setting size of y, will probably have to erase elements within array

        //NOTE: array based 1, probably will have to change to base 0 MISTAKE???
        for(int j = x.getSize() + 1; j >= i + 1; j--)
        {
            x.childPointers[j+1] = x.childPointers[j];
        }

        writeNode(z, true);//Disk-Write (z) Moved up because z needs an address before x can point to it
        z = readNode(lastWriteLocation);
        x.childPointers[i + 1] = z.getAddress();

        for(int j = x.getSize() - 1; j >= i; j--)
        {
            x.elements[j + 1] = x.elements[j];
        }

        x.elements[i] = y.elements[t - 1];
        x.setSize(x.getSize() + 1);

        writeNode(x, false);//Disk-Write (x)
        writeNode(y, false);//Disk-Write (y)
    }

    /**
     *
     * This method is only called from within the InsertKey method if a node has one
     * or more open spots for new BTreeElements contained by that node.
     *
     * First a check is done to see if the BTreeNode is a leaf, which affects the
     * insertion process. The correct location for the new BTree element is
     * found, or if a matching value already exists the number of duplicates is
     * increased. Variables related to the BTreeNode are updated.
     *
     * @param {BTreeNode} x
     * @param {BTreeElement} k
     *
     */
    private void bTreeInsertNonfull(BTreeNode x, BTreeElement k) throws IOException
    {
        int i = x.getSize() - 1;

        if(x.isLeaf())
        {
//            //TEMP: Runs in linear time and has very ugly flow of control, only using for testing.
//            for(int n = 0; n < x.getSize(); n++)
//            {
//                if(x.elements[n].key == k.key)
//                {
//                    x.elements[n].incrementDuplicates();
//                    this.totalDuplicates++;
//                    writeNode(x, false);
//                    return;
//                }
//            }
            while(i >= 0 && k.key < x.elements[i].key)
            {
                x.elements[i + 1] = x.elements[i];
                i--;

            }

            x.elements[i + 1] = k;
            x.setSize(x.getSize() + 1);

            writeNode(x, false);//Disk-write(x)
        }

        else
        {
            while(i >= 0 && k.key < x.elements[i].key)
            {
                //x.elements[i + 1] = x.elements[i];
                i--;
            }
//            if(i > -1 && k.key == x.elements[i].key)
//            {
//                x.elements[i].incrementDuplicates();
//                writeNode(x, false);
//                return;
//            }
            i++;
            //Disk-Read(x.children[i])

            if(readNode(x.childPointers[i]).getSize() == (2*t - 1))
            {
                bTreeSplitChild(x, i);

                if(k.key > x.elements[i].key)
                {
                    i++;
                }
            }

            //Disk-Read(x.children[i]): this is in piazza slides, does not match up with any other pseudo code
            bTreeInsertNonfull(readNode(x.childPointers[i]), k);

        }
    }

    /**
     * Iterates over the BTree starting at the given node.
     *
     * @param {BTreeNode} The node to start at
     * @return {ArrayDeque<BTreeElement>} The returned iterated element
     * @throws IOException
     */
    public ArrayDeque<BTreeElement> iterateBTree(BTreeNode node) throws IOException {
        ArrayDeque<BTreeNode> start = new ArrayDeque<BTreeNode>();
        ArrayDeque<BTreeElement> finish = new ArrayDeque<BTreeElement>();

        start.add(node);

        while (!start.isEmpty()) {
            BTreeNode current = start.remove();

            for (int i = 0; i < current.getSize(); i++) {

                finish.add(current.elements[i]);
            }

            int i = 0;
            //while (i < current.childPointers.length-1)
            while (current.childPointers[i] != 0 && current.childPointers[i] != -1)
            {
                start.add(readNode(current.childPointers[i]));
                i++;
            }
        }

        return finish;
    }

    public BTreeNode getRoot()
    {
        return root;
    }

    /**
     * This method returns the root of a BTree object.
     *
     * @return {BTreeNode} root
     */
    public void setRoot(BTreeNode root) throws IOException
    {
        this.root = root;
        this.rootAddress = root.getAddress();
        long currentPos = dataFile.getFilePointer();
        dataFile.seek(4);
        dataFile.writeLong(rootAddress);
        dataFile.seek(currentPos);
    }
}