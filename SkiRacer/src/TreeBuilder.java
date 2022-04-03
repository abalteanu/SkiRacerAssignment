/**
 * 
 */

/**
 * This class builds the binary tree for the ski race using queues. It follows a simple implementation as a tree traversal, however instead of traversing
 * the tree, it builds the tree. 
 * Uses generic types to store data of any type. 
 * @author Ana Balteanu
 *
 */
public class TreeBuilder<T> {
	
	//No instance variables or constructors (since there is nothing to initialize
	
	/**
	 * Takes in array data which contains an array of data to be stored into the binary tree. Each element will be set into one of the tree's new nodes.
	 * Elements that are null indicate no node to be created in that position
	 * Two queues are used: dataQueue of type LinkedQueue<T> and parentQueue of type LinkedQueue<BinaryTreeNode<T>
	 * 
	 * @param data
	 * @return
	 */
	public LinkedBinaryTree<T> buildTree (T[] data){
		
		LinkedQueue<T> dataQueue = new LinkedQueue<T>();
		
		// filling each node with the elements from the data array
		for(int i = 0; i < data.length; i++) {
			dataQueue.enqueue(data[i]);
		}
		
		// initializing empty parentQueue which will hold a list of Binary tree nodes
		LinkedQueue<BinaryTreeNode<T>> parentQueue = new LinkedQueue<BinaryTreeNode<T>>();
		
		// making root node
		BinaryTreeNode<T> rootNode = new BinaryTreeNode<T>(dataQueue.dequeue());
		
		// making the root node a node on the tree that stores the first element in dataQueue
		LinkedBinaryTree<T> tree = new LinkedBinaryTree<T>(rootNode);
		
		// enqueueing the root node onto parent queue
		parentQueue.enqueue(rootNode);
		
		while(!dataQueue.isEmpty()) {
			// while the dataQueue still has elements, go through each element to build the tree
			
			BinaryTreeNode<T> a = new BinaryTreeNode<T>(dataQueue.dequeue());
			BinaryTreeNode<T> b = new BinaryTreeNode<T>(dataQueue.dequeue());
			
			BinaryTreeNode<T> parent = parentQueue.dequeue();
			
			if(a.getData() != null) {
				// add node storing a as left child of parent and enqueue on parentQueue
				parent.setLeft(a);
				parentQueue.enqueue(a);
			} 
			
			if(b.getData() != null) {
				// add node storing b as right child of parent and enqueue on parentQueue
				parent.setRight(b);
				parentQueue.enqueue(b);
			}
			
		}
		
		return tree;
		
	}


}
