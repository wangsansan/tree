package redBlackTree;

public class Node {

	static boolean RED = false;
	static boolean BLACK = true;

	int value;
	private Node left;
	private Node right;
	private Node father;
	private boolean color = RED;
	private Node brother;

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

	public Node getBrother() {
		return brother;
	}

	public void setBrother(Node brother) {
		this.brother = brother;
	}

	public Node(int value) {
		this.value = value;
	}

	public boolean getColor() {
		return color;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Node [value=" + value + ", left=" + left.getValue()
				+ ", right=" + right.getValue() + ", father="
				+ father.getValue() + ", color=" + color + ", brother="
				+ brother.getValue() + "]";
	}

}
