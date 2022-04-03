/**
 * 
 */

/**
 * File guiding racer through the hill in the most optimal path. 
 * Path options inlcude null (no child node), an empty string "" (regular node), a jump segment "jump-#" with a given jump number, a slalom segment in
 * the leeward direction "slalom-L", a slalom segment in the windward direction "slalom-W"
 * 
 * @author Ana Balteanu
 *
 */
public class Ski {
	
	private BinaryTreeNode<SkiSegment> root;
	
	/**
	 * Private helper method that checks if both next nodes are jumps GIVEN that left is already known to be a jump
	 * If right turns out not to be a jump, left is returned
	 * @param r is the right node
	 * @param l is the left node
	 * @param chosen is the node chosen by the end of the method
	 * @return chose, which is the node determined by the function
	 */
	private BinaryTreeNode<SkiSegment> checkBothJumps(BinaryTreeNode<SkiSegment> r, BinaryTreeNode<SkiSegment> l, BinaryTreeNode<SkiSegment> chosen) {
		
		if(r.getData() instanceof JumpSegment) {
			// if both left and right are jump segments
			int rHeight, lHeight;
			// cast to jumpSegment so compiler knows its JumpSegment
			rHeight = ((JumpSegment)l.getData()).getHeight();
			lHeight = ((JumpSegment)r.getData()).getHeight();
			
			if(rHeight >= lHeight) {
				// if the right jump is greater than or equal to the left jump, choose right
				chosen = r;
			} else {
				// otherwise choose left
				chosen = l;
			}
		} else {
			// if right is not a jump, go left
			chosen = l;
		}
		
		return chosen;
	}
	
	/**
	 * Private helper method to determine if both options of nodes are slalom GIVEN that left is slalom
	 * if right is not slalom, further action is taken to determine which direction to choose
	 * @param r is the right node
	 * @param l is the left node
	 * @param chosen is the node chosen by the end of the method
	 * @return chose, which is the node determined by the function
	 */
	private BinaryTreeNode<SkiSegment> checkBothSlaloms(BinaryTreeNode<SkiSegment> r, BinaryTreeNode<SkiSegment> l, BinaryTreeNode<SkiSegment> chosen) {
		
		// finding direction of left slalom
		// cast to SlalomSegment so compiler knows its SlalomSegment

		String ldir = ((SlalomSegment)l.getData()).getDirection();

		if(r.getData() instanceof SlalomSegment) {
			// if both left and right are jump segments
			String rdir;
			rdir = ((SlalomSegment)r.getData()).getDirection();
			
			if(rdir.equals("L")) {
				// if the right slalom is leeway, choose right
				chosen = r;
			} else {
				// otherwise choose left
				chosen = l;
			}
		} else {
			// if right is not a slalom, it is a regular, check if left is leeway
			chosen = chooseOneSlalom(l, r, chosen);
		}
		
		return chosen;
	}
	
	/**
	 * Private helper method to determine if the slalom segment should be chosen over the regular segment
	 * Depends on if slalom is leeward or windward
	 * @param slalom is the slalom node
	 * @param reg is the regular node
	 * @param chosen is the node chosen by the end of the function
	 * @return chosen node
	 */
	private BinaryTreeNode<SkiSegment> chooseOneSlalom(BinaryTreeNode<SkiSegment> slalom, BinaryTreeNode<SkiSegment> reg, BinaryTreeNode<SkiSegment> chosen) {
		
		if(((SlalomSegment)slalom.getData()).getDirection().equals("L")){
			chosen = slalom;
		} else {
			chosen = reg;
		}
		return chosen;
	}

	
	/**
	 * Constructor that takes in the string array of data and makes a new segments array with sepcific types
	 * @param data, a string array with information about the node types
	 */
	public Ski(String[ ] data) {
		SkiSegment segments[] = new SkiSegment[data.length];
		
		for(int i = 0; i < data.length; i++) {
			// assigning specific types to the data items in data, and storing them in stegments array
			String nodeType = data[i];
			
			if(nodeType == null) {
				segments[i] = new SkiSegment(String.valueOf(i),data[i]);
			}
			else if(nodeType.contains("jump")) {
				segments[i] = new JumpSegment(String.valueOf(i),data[i]);
			} else if(nodeType.contains("slalom")) {
				segments[i] = new SlalomSegment(String.valueOf(i),data[i]);
			} else if(nodeType.equals("")) {
				segments[i] = new SkiSegment(String.valueOf(i),data[i]);
			}
		}
		
		// using TreeBuilder to build tree, then putting the built tree into Linked Binary Tree
		TreeBuilder<SkiSegment> skiHillBuilder = new TreeBuilder<SkiSegment>();
		LinkedBinaryTree<SkiSegment> skiHill = skiHillBuilder.buildTree(segments);
		
		// storing root
		this.root = skiHill.getRoot();
	}
	
	/**
	 * Getter method that returns the instance variable root
	 * @return root, a variable storing the root of the ski tree
	 */
	public BinaryTreeNode<SkiSegment> getRoot(){
		return this.root;
	}
	
	
	/**
	 * Recursive method which calls itself to continue finding the path (next node)
	 * Base case is when a node is null
	 * Determines next node to be accessed
	 */
	public void skiNextSegment (BinaryTreeNode<SkiSegment> node, ArrayUnorderedList<SkiSegment> sequence) {
		
		if(node == null) return;
		
		// 0 path: base case is when the node you are on is null, in which case you are done
		
		sequence.addToRear(node.getData()); 	// tracking path by adding the node's data to end of sequence


		// getting the two next nodes
		BinaryTreeNode<SkiSegment> left = node.getLeft();
		BinaryTreeNode<SkiSegment> right = node.getRight();
		BinaryTreeNode<SkiSegment> next = right;	// next will be chosen by the function later on, but right by default 
		
		/// Deciding next path
		
		// 1 path option: if either left or right is null, then choose the node that is not null. 
		if(left == null) {
			next = right;	// check case where next is null.. it should be fine because if next node is null it'll catch that in the next iteration
		} else if (right == null) {
			next = left;
		} else {
			// 2 path option: if both L and R are not null
			
			if(left.getData() instanceof JumpSegment) {
				/// jumps
				// if left is a jump
				next = checkBothJumps(right, left, next); // checks if right is a jump as well and picks between l and r
				
			} else if (right.getData() instanceof JumpSegment) {
				// if left is not a jump and right is a jump
				next = right;	// choose right
				
			} else if (left.getData() instanceof SlalomSegment) {
				/// no jumps, but segments
				// if left is a slalom
				next = checkBothSlaloms(right, left, next); // checks if right is a slalom as well and picks between l and r
				
			} else if (right.getData() instanceof SlalomSegment) {
				// if left is not a slalom and right is a slalom
				// find if slalom is leeway, then choose leeway, if not, choose reg
				next = chooseOneSlalom(right, left, next);
			} 
			
			// if both are regular, default value is already set to right
		}
		
		skiNextSegment(next, sequence);	
		
		
		
		
	}
}
