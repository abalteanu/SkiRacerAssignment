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
	 * 
	 * @param data, a string array with information about the node types
	 */
	public Ski(String[ ] data) {
		SkiSegment segments[] = new SkiSegment[data.length];
		
		for(int i = 0; i < data.length; i++) {
			// assigning specific types to the data items in data, and storing them in stegments array
			String nodeType = data[i];
			
			if(nodeType.contains("jump")) {
				segments[i] = new JumpSegment(String.valueOf(i),data[i]);
			} else if(nodeType.contains("slalom")) {
				segments[i] = new SlalomSegment(String.valueOf(i),data[i]);
			} else if(nodeType.equals("")) {
				segments[i] = new SkiSegment(String.valueOf(i),data[i]);
			} else {
				// null case
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
	 * Getter method that returns the instance variabel root
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
		
		sequence.addToRear(node.getData()); 	// tracking path by adding the node's data to end of sequence

		while(node.getData() != null) {	// 0 path: base case is when the node you are on is null, in which case you are done
			
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
				
				// if jumps are included
				if(left.getData() instanceof JumpSegment) {
					// if left is a jump
					next = checkBothJumps(right, left, next); // checks if right is a jump as well and picks between l and r
					
				} else if (right.getData() instanceof JumpSegment) {
					// if left is not a jump and right is a jump
					next = right;
				}
				
				// no jumps, but segments
				
			}
			
			
			
			
			skiNextSegment(next, sequence);
			
		}
		
		
		
	}
}
