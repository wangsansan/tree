package balanceTree;

import sortedTree.SortedTree;
import node.Node;

public class BalanceTree1 {

	static SortedTree tree = new SortedTree();
	
	public Node buildBalanceTree(int[] values) {
		Node root = new Node(values[0]);
		Node parent = null;
		Node node = null;
		for (int i = 1; i < values.length; i++) {
			node = new Node(values[i]);
			parent = root;
			while (true) {
				if (node.getValue() >= parent.getValue())
					if (parent.getRight() != null) {
						parent = parent.getRight();
					} else
						break;
				else if (parent.getLeft() != null) {
					parent = parent.getLeft();
				} else
					break;
			}

			if (node.getValue() >= parent.getValue())
				parent.setRight(node);
			else
				parent.setLeft(node);

			node.setFather(parent);

			// 以上完成的是每个节点按照二叉树的顺序插入
			Node unBalacedNode = findUnbalancedNode(node);
			if (unBalacedNode != null) {
				Node unFather = unBalacedNode.getFather();
				if (getDifHeight(unBalacedNode) > 0) {
					if (getDifHeight(unBalacedNode.getLeft()) < 0) {
						// LR
						Node lNode = unBalacedNode.getLeft();
						unBalacedNode.setLeft(null);
						lNode.setFather(null);
						Node lrNode = lNode.getRight();
						lNode.setRight(null);
						lrNode.setFather(unFather);
						if (unFather == null)
							root = lrNode;
						else {
							if (unFather.getLeft() == unBalacedNode) {
								unFather.setLeft(lrNode);
							} else {
								unFather.setRight(lrNode);
							}
						}
						lrNode.setRight(unBalacedNode);
						unBalacedNode.setFather(lrNode);
						lrNode.setLeft(lNode);
						lNode.setFather(lrNode);
					} else {
						// LL；判断何种不平衡出错了。
						Node lNode = unBalacedNode.getLeft();
						lNode.setFather(unFather);
						unBalacedNode.setLeft(null);
						Node lrNode = lNode.getRight();
						if (lrNode != null)
							lrNode.setFather(unBalacedNode);
						if (unFather == null)
							root = lNode;
						else {
							if (unFather.getLeft() == unBalacedNode) {
								unFather.setLeft(lNode);
							} else {
								unFather.setRight(lNode);
							}
						}
						lNode.setRight(unBalacedNode);
						unBalacedNode.setFather(lNode);
						unBalacedNode.setLeft(lrNode);
					}
				} else {
					if (getDifHeight(unBalacedNode.getRight()) < 0) {
						// RR
						Node rNode = unBalacedNode.getRight();
						unBalacedNode.setRight(null);
						rNode.setFather(unFather);
						Node rlNode = rNode.getLeft();
						if (rlNode != null)
							rlNode.setFather(unBalacedNode);
						if (unFather == null)
							root = rNode;
						else {
							if (unFather.getLeft() == unBalacedNode)
								unFather.setLeft(rNode);
							else
								unFather.setRight(rNode);
						}
						rNode.setLeft(unBalacedNode);
						unBalacedNode.setFather(rNode);
						unBalacedNode.setRight(rlNode);
					} else {
						// RL
						Node rNode = unBalacedNode.getRight();
						Node rlNode = rNode.getLeft();
						rlNode.setFather(unFather);
						if (unFather == null)
							root = rlNode;
						else {
							if (unFather.getLeft() == unBalacedNode) {
								unFather.setLeft(rlNode);
							} else {
								unFather.setRight(rlNode);
							}
						}
						rlNode.setLeft(unBalacedNode);
						unBalacedNode.setFather(rlNode);
						rlNode.setRight(rNode);
						rNode.setFather(rlNode);
						unBalacedNode.setRight(null);
						rNode.setLeft(null);
					}
				}
			}

			tree.showTreeByWidth(root);
			System.out.println("====================================");
		}

		return root;
	}

	public Node findUnbalancedNode(Node node) {
		if (node == null || node.getFather() == null
				|| node.getFather().getFather() == null)
			return null;
		Node ret = node.getFather().getFather();
		while (true) {
			if (isBalacedTree(ret))
				if (ret.getFather() != null)
					ret = ret.getFather();
				else
					return null;
			else
				break;
		}
		return ret;
	}

	public boolean isBalacedTree(Node node) {
		if (getDifHeight(node) <= 1 && getDifHeight(node) >= -1)
			return true;
		return false;
	}

	public int getDifHeight(Node node) {
		return getHeight(node.getLeft()) - getHeight(node.getRight());
	}

	public int getHeight(Node node) {
		if (node == null)
			return 0;
		if (node.getLeft() == null && node.getRight() == null)
			return 1;
		return getHeight(node.getLeft()) >= getHeight(node.getRight()) ? getHeight(node
				.getLeft()) + 1 : getHeight(node.getRight()) + 1;
	}

	public static void main(String[] args) {
		BalanceTree1 balanceTree = new BalanceTree1();
		// int[] values = { 8, 6, 11, 5, 4, 7, 9, 12 };
		// Node root = tree.buildSortedTree(values);
		// int height = balanceTree.getHeight(root);
		int[] values = { 16, 3, 7, 11, 9, 26, 18, 14, 15 };
		balanceTree.buildBalanceTree(values);
	}

}
