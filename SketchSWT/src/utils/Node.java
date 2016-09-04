package utils;

public class Node {
    public Object data;
    public Node parent;
    public Node leftChild;
    public Node rightChild;
    
    public Node() {
    	
    }
    
    public Node(Object data) {
    	this.data = data;
    }
    
    public Node(Object data, Node leftChild, Node rightChild) {
    	this.data = data;
    	this.setLeftChild(leftChild);
    	this.setRightChild(rightChild);
    }
    
    public void setLeftChild(Node node) {
    	if(leftChild != null)
    		leftChild.parent = null;
    	leftChild = node;
    	leftChild.parent = this;
    }
    
    public void setLeftChild(Object data) {
    	Node node = new Node();
    	node.data = data;
    	setLeftChild(node);
    }
    
    public void setRightChild(Node node) {
    	if(rightChild != null)
    		rightChild.parent = null;
    	rightChild = node;
    	rightChild.parent = this;
    }
    
    public void setRightChild(Object data) {
    	Node node = new Node();
    	node.data = data;
    	setRightChild(node);
    }
    
    public boolean isLeaf() {
    	return leftChild==null && rightChild==null;
    }
    
    public String toString() {
    	if(isLeaf())
    		return data.toString();
    	else
    		return "[" + leftChild.toString() + " " + data.toString() + " " + rightChild.toString() + "]"; 
    }
}
