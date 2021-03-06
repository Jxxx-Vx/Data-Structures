package linked;


public class DNALinkedList 
{
	private Node<Character>			head;	// head.prev is always null
	private Node<Character>			tail;	// tail.next is always null
	
	
	public DNALinkedList(String s)
	{
		append(s);		
	}
	
	
	// Used by extraction methods. Not for public use.
	private DNALinkedList(Node<Character> head, Node<Character> tail)
	{ 
		this.head = head;
		head.setPrev(null);
		this.tail = tail;
		tail.setNext(null);
	}
	
	
	// Converts arg to nodes which are appended to end of this list.
	public void append(String s)
	{
		for (int i=0; i<s.length(); i++)
			append(s.charAt(i));
	}
	
	
	// Creates a node for ch and appends it to the linked list.
	// "Append" always means "at the end".
	public void append(char ch)
	{
		append(new Node<Character>(ch));
	}
	
	
	// Appends n to tail of this list.
	public void append(Node<Character> n)
	{
		// Corner case: empty list.
		if (tail == null)
		{
			n.setPrev(null);
			n.setNext(null);
			head = n;
			tail = n;
		}
		
		// Vanilla case.
		else
		{
			tail.setNext(n);
			n.setPrev(tail);
			tail = n;
		}
	}
	
	
	public String toString()
	{
		String s = "DNALinkedList: ";
		if (head == null)
			s += "Empty";
		else
		{
			Node<Character> n = head;
			while (n != null)
			{
				s += n.getData();
				n = n.getNext();
			}
		}
		return s;
	}
	
	
	// Returns true if the nodes starting at startNode match the target string. For example,
	// if the linked list looks like this:
	// (A) —> (B) -> (C) -> (D) -> (E) -> (F)
	// and if startNode is the 2nd node (“B”), then matches(startNode, “BCDE”) should return true.
	private boolean matches(Node<Character> startNode, String target)
	{
	Node<Character> match = startNode;
		for(int n = 0; n < target.length(); n++) {
			if(match.getData() != target.charAt(n))
				return false;
			if(match.getNext() == null && match.getData() != target.charAt(target.length()-1)) // if at tail and isn't at end of target 
				return false;
			match = match.getNext();
		}
	return true;	
	}
	
	
	// If this list contains a chain of nodes whose data is the target, returns
	// the node at the start of that chain. If the target appears multiple times
	// in this list, returns the first occurrence. If the target is not in this list,
	// returns null.
	public Node<Character> find(String target)
	{
		// Hint: call matches(). A lot.
		Node<Character> startNode = head;
		while(startNode != null) {
			if(matches(startNode, target))
				return startNode;
			startNode = startNode.getNext();
		}
		return null;
	}
	
	
	// Extract and return the nodes starting at firstExtractedNode and ending at lastExtractedNode.
	// The returned nodes should be a DNALinkedList. Don’t worry about any corner cases: assume
	// firstExtractedNode and lastExtractedNode are both in the list and are not near the head or tail.
	public DNALinkedList extract(Node<Character> firstExtractedNode, Node<Character> lastExtractedNode)
	{
		// Find nodes just before and just after the chain to be extracted. Assume these
		// aren't null.
		Node<Character> beforeFirst = firstExtractedNode.getPrev();
		Node<Character> afterLast = lastExtractedNode.getNext();
		
		// Connect beforeFirst to afterLast.
		beforeFirst.setNext(afterLast);
		afterLast.setPrev(beforeFirst);
		
		// Return a DNALinkedList containing the extracted chain.
		return new DNALinkedList(firstExtractedNode, lastExtractedNode);
	}

	
	// Reverses the order of the nodes.
	public void reverse()
	{
		// Swap next and prev of every node. Caution: in your loop, you won't be able
		// to advance n by setting n = n.next(). Why? How should you advance n?
		Node<Character> n = head;
		while (n != null)
		{
			Node<Character> storeNext = n.getNext();
			
			n.setNext(n.getPrev());
			n.setPrev(storeNext);
			n = n.getPrev();
		}
		
		// Swap head and tail.
		Node<Character> temp = head;
		head = tail;
		tail = temp;
	}
	
	
	// Inserts insertMe into this list, at the node before insertionPoint. Assumes
	// insertionPoint is not the head or tail. 
	public void insert(DNALinkedList insertMe, Node<Character> insertionPoint)
	{
		// Find node immediately before insertion point.
		Node<Character> beforeInsertionPoint = insertionPoint.getPrev();
		
		// Connect node immediately before insertion point to head of insertMe.
		beforeInsertionPoint.setNext(insertMe.head);
		insertMe.head.setPrev(beforeInsertionPoint);
		
		// Connect tail of insertMe to insertionPoint node.
		insertMe.tail.setNext(insertionPoint);
		insertionPoint.setPrev(insertMe.tail);
	}
	

	// Removes sequence matching transposon, reverses it, and inserts it back into
	// this list immediately before target. Throws IllegalArgumentException if
	// can't find transposon or target. Use the methods you just wrote.
	public void transpose(String transposon, String target)
	{
		// Find starting node of transposon.
		Node<Character> firstNodeOfTransposon = find(transposon);
		if (firstNodeOfTransposon == null)
			throw new IllegalArgumentException("Couldn't find transposon: " + transposon);
		
		// Find starting node of target.
		Node<Character> firstNodeOfTarget = find(target);
		if (firstNodeOfTarget == null)
			throw new IllegalArgumentException("Coudln't find target: " + target);
		
		// Find last node of transposon. You'll need several lines. Set a variable to the first node
		// of the transposon, then do a loop where for every char in the transposon, you set the variable
		// to its "next".
		Node<Character> lastNodeOfTransposon = firstNodeOfTransposon;
		
		for(int n = 0; n < transposon.length()-1; n++) {
			lastNodeOfTransposon = lastNodeOfTransposon.getNext();
		}
		 
				
		// Extract the transposon.
		DNALinkedList transposonList = extract(firstNodeOfTransposon, lastNodeOfTransposon);
		
		// Reverse the transposon.
		transposonList.reverse();
		
		// Insert immediately before target.
		insert(transposonList, firstNodeOfTarget);
	}
		
	
	public static void main(String[] args) 
	{
		String chromosome = "ABCDE";
		DNALinkedList list = new DNALinkedList(chromosome);
		System.out.println("original: " + list);
		String transposon = "BC";
		String target = "E";
		list.transpose(transposon, target);
		System.out.println("transposed: " + list);
	}
}
