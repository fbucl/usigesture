package utils;

public class BinaryTree {
	private Node root;

    public BinaryTree(Object rootData) {
        root = new Node();
        root.data = rootData;
    }
    
    public BinaryTree(Node root) {
    	this.root = root;
    }
    
    public Node getRoot() {
    	return root;
    }
    
    public String toString() {
    	return root.toString();
    }
   
}
