package node;

public class Node {
	int value;
	private Node left;
	private Node right;
	private Node father;

	public Node() {
		// TODO Auto-generated constructor stub
		this(-1);
	}

	public Node(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	public Node getFather() {
		return father;
	}

	public void setFather(Node father) {
		this.father = father;
	}

	@Override
	public String toString() {
		return "Node [value=" + value + ", left=" + left.value + ", right="
				+ right.value + "]";
	}

}
