package sortedTree;

import java.util.LinkedList;
import java.util.List;

import node.Node;

public class SortedTree {

	public Node buildSortedTree(int[] values) {
		Node root = null;
		root = new Node(values[0]);
		Node parent = null;
		for (int i = 1; i < values.length; i++) {
			Node node = new Node(values[i]);
			parent = root;
			while (true) {
				if (node.getValue() >= parent.getValue())
					if (parent.getRight() != null)
						parent = parent.getRight();
					else
						break;
				else if (parent.getLeft() != null)
					parent = parent.getLeft();
				else
					break;
			}

			if (node.getValue() >= parent.getValue())
				parent.setRight(node);
			else
				parent.setLeft(node);
		}
		return root;
	}

	public void showTreeByWidth(Node root) {
		List<Node> nodes = new LinkedList<>();
		nodes.add(root);
		int current = 1;
		showTreeByWidth(nodes,current);
	}

	public void showTreeByWidth(List<Node> nodes,int current) {
		int next = 0;
		while (current != 0) {
			Node node = nodes.get(0);
			System.out.print(node.getValue() + " ");
			if (node.getLeft() != null) {
				next++;
				nodes.add(node.getLeft());
			}
			if (node.getRight() != null) {
				next++;
				nodes.add(node.getRight());
			}
			nodes.remove(0);
			current--;
		}
		
		System.out.println();//»»ÐÐ
		
		if(next == 0)
			return;

		current = next;
		showTreeByWidth(nodes,current);
	}
	

	public static void main(String[] args){
		SortedTree tree = new SortedTree();
		int[] values = {8,6,11,4,7,9,12};
		Node root = tree.buildSortedTree(values);
		tree.showTreeByWidth(root);
	}
	
}
