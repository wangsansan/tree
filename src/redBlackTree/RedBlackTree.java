package redBlackTree;

import java.util.LinkedList;
import java.util.List;

import sortedTree.SortedTree;

public class RedBlackTree {
	/*
	 * I、红黑树的五个性质：
	 *  1）每个结点要么是红的，要么是黑的。
	 *  2）根结点是黑的。 
	 *  3）每个叶结点，即空结点（NIL）是黑的。
	 *  4）如果一个结点是红的，那么它的俩个儿子都是黑的。 
	 *  5）对每个结点，从该结点到其子孙结点的所有路径上包含相同数目的黑结点。
	 */

	Node root = null;
	SortedTree tree = new SortedTree();

	public RedBlackTree() {
	}

	public RedBlackTree(int[] values) {
		for (int i = 0; i < values.length; i++)
			addNode(values[i]);
	}

	public void deleteNode(int value) {
		Node toDeleteNode = getNode(value);
		if (toDeleteNode == null)
			return;
		boolean left = true;
		boolean right = !left;
		if (toDeleteNode.getFather() != null) {
			if (toDeleteNode.getValue() > toDeleteNode.getFather().getValue()) {
				right = true;
				left = !right;
			}
		} else {
			toDeleteNode = root;
		}

		if (toDeleteNode.getColor() == Node.RED) {
			if (toDeleteNode.getLeft() == null) {
				/*
				 * 因为红黑树性质的限定，红色节点要么没有子树，要么有两个黑色节点孩子 所以此处判断一个是否是空即达到判断两个是否为空
				 */
				deleteRedLeaf(toDeleteNode, left);
			}
		} else {
			if (toDeleteNode.getLeft() == null)
				;
		}
	}

	public void deleteRedLeaf(Node toDeleteNode, boolean left) {
		Node father = toDeleteNode.getFather();
		if (left) {
			father.setLeft(null);
		} else {
			father.setRight(null);
		}
	}

	public Node getNode(int value) {
		Node node = root;
		while (true) {
			if (node.getValue() == value) {
				break;
			} else {
				if (node.getValue() < value) {
					if (node.getRight() != null)
						node = node.getRight();
					else {
						node = null;
						break;
					}
				} else {
					if (node.getLeft() != null)
						node = node.getLeft();
					else {
						node = null;
						break;
					}
				}
			}
		}
		return node;
	}

	public void addNode(int value) {
		Node node = new Node(value);
		if (root == null) {
			root = node;
			root.setColor(Node.BLACK);
			return;
		}
		Node parent = toAddPostionPrarent(value);

		if (parent == null)
			return;

		if (parent.getValue() > value) {
			parent.setLeft(node);
			if (parent.getRight() != null) {
				node.setBrother(parent.getRight());
				parent.getRight().setBrother(node);
			}
		} else {
			parent.setRight(node);
			if (parent.getLeft() != null) {
				node.setBrother(parent.getLeft());
				parent.getLeft().setBrother(node);
			}
		}

		node.setFather(parent);

		// 节点放完了，该调整红黑树顺序了

		adjustTreeToRedBlackBalance(node);

		showTreeByWidth(root);
		System.out.println("================================");
	}

	public void adjustTreeToRedBlackBalance(Node node) {
		while (true) {
			if (node.getFather().getColor() == Node.RED) {
				if (node.getFather().getBrother() != null
						&& node.getFather().getBrother().getColor() == Node.RED) {
					{
						node.getFather().setColor(Node.BLACK);
						node.getFather().getBrother().setColor(Node.BLACK);
						if (root == node.getFather().getFather())
							break;
						else {
							node = node.getFather().getFather();
							node.setColor(Node.RED);
						}
					}
				} else {
					Node grandFather = node.getFather().getFather();
					if (grandFather.getLeft() == node.getFather()
							&& node == node.getFather().getRight())
						LRRound(node);
					if (grandFather.getRight() == node.getFather()
							&& node == node.getFather().getLeft())
						RLRound(node);
					if (grandFather.getLeft() == node.getFather()
							&& node == node.getFather().getLeft())
						LLRound(node);
					if (grandFather.getRight() == node.getFather()
							&& node == node.getFather().getRight())
						RRRound(node);
				}
			} else {
				break;
			}
		}
	}

	public void LRRound(Node node) {
		Node grandFather = node.getFather().getFather();
		grandFather.setLeft(node);
		node.setLeft(node.getFather());
		node.setFather(grandFather);
		node.getLeft().setFather(node);
		node = node.getLeft();
		node.setBrother(null);
	}

	public void RLRound(Node node) {
		Node grandFather = node.getFather().getFather();
		grandFather.setRight(node);
		node.setRight(node.getFather());
		node.setFather(grandFather);
		node.getRight().setFather(node);
		node = node.getRight();
		node.setBrother(null);
	}

	public void LLRound(Node node) {
		node.getFather().setColor(Node.BLACK);
		Node grandFather = node.getFather().getFather();
		Node greatGrandFather = grandFather.getFather();
		grandFather.setColor(Node.RED);
		if (greatGrandFather == null) {
			root = node.getFather();
		} else {
			if (greatGrandFather.getLeft() == grandFather) {
				greatGrandFather.setLeft(node.getFather());
			} else
				greatGrandFather.setRight(node.getFather());
			node.getFather().setBrother(grandFather.getBrother());
		}
		node.getFather().setFather(greatGrandFather);
		node.getFather().setRight(grandFather);
		grandFather.setFather(node.getFather());
		if (node.getBrother() != null)
			node.getBrother().setFather(grandFather);
		grandFather.setLeft(node.getBrother());
		if (grandFather.getRight() != null)
			grandFather.getRight().setBrother(node.getBrother());
		node.setBrother(grandFather);
		grandFather.setBrother(node);
	}

	public void RRRound(Node node) {
		node.getFather().setColor(Node.BLACK);
		Node grandFather = node.getFather().getFather();
		Node greatGrandFather = grandFather.getFather();
		grandFather.setColor(Node.RED);
		if (greatGrandFather == null) {
			root = node.getFather();
		} else {
			if (greatGrandFather.getLeft() == grandFather)
				greatGrandFather.setLeft(node.getFather());
			else
				greatGrandFather.setRight(node.getFather());
			node.getFather().setBrother(grandFather.getBrother());
		}
		node.getFather().setFather(greatGrandFather);
		node.getFather().setLeft(grandFather);
		grandFather.setFather(node.getFather());
		if (node.getBrother() != null)
			node.getBrother().setFather(grandFather);
		grandFather.setRight(node.getBrother());
		if (grandFather.getLeft() != null)
			grandFather.getLeft().setBrother(node.getBrother());
		node.setBrother(grandFather);
		grandFather.setBrother(node);
	}

	public Node toAddPostionPrarent(int value) {
		Node parent = root;
		while (true) {
			if (value > parent.getValue()) {
				if (parent.getRight() != null)
					parent = parent.getRight();
				else
					break;
			} else {
				if (value == parent.getValue()) {
					parent = null;
					break;
				} else {
					if (parent.getLeft() != null)
						parent = parent.getLeft();
					else
						break;
				}
			}
		}

		return parent;
	}

	public void showTreeByWidth(Node root) {
		List<Node> nodes = new LinkedList<>();
		nodes.add(root);
		int current = 1;
		showTreeByWidth(nodes, current);
	}

	public void showTreeByWidth(List<Node> nodes, int current) {
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

		System.out.println();// 换行

		if (next == 0)
			return;

		current = next;
		showTreeByWidth(nodes, current);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] values = { 1, 2, 3, 4, 5, 6, 7, 8 };
		RedBlackTree redBlackTree = new RedBlackTree(values);
	}

}
