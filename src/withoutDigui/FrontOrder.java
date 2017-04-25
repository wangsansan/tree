package withoutDigui;

import java.util.Stack;

import sortedTree.SortedTree;
import node.Node;

public class FrontOrder {

	public void frontOrder(Node root) {
		Stack<Node> stack = new Stack<>();
		Node node = root;
		while (stack.size() != 0 || node != null) {
			if (node != null) {
				stack.push(node);
				System.out.print(node.getValue() + " ");
				node = node.getLeft();
			} else {
				Node node2 = stack.pop();
				node = node2.getRight();
			}
		}
	}

	public void frontOrder1(Node root) {
		Stack<Node> stack = new Stack<>();
		Node node = root;
		while (stack.size() != 0 || node != null) {
			while (node != null) {
				stack.push(node);
				System.out.print(node.getValue() + " ");
				node = node.getLeft();
			}
			Node node2 = stack.pop();
			node = node2.getRight();
		}
	}

	public void midOrder(Node root) {
		Stack<Node> stack = new Stack<>();
		Node node = root;
		while (stack.size() != 0 || node != null) {
			if (node != null) {
				stack.push(node);
				node = node.getLeft();
			} else {
				Node node2 = stack.pop();
				System.out.print(node2.getValue() + " ");
				node = node2.getRight();
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SortedTree tree = new SortedTree();
		FrontOrder order = new FrontOrder();
		int[] values = { 8, 3, 6, 9, 2, 10, 4 };
		Node root = tree.buildSortedTree(values);
		 order.frontOrder1(root);
//		order.midOrder(root);
	}

}
