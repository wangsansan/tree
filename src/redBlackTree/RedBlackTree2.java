package redBlackTree;

import java.util.LinkedList;
import java.util.List;

import sortedTree.SortedTree;

public class RedBlackTree2 {
	
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

	public RedBlackTree2() {
	}

	public RedBlackTree2(int[] values) {
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
				deleteRedLeaf(toDeleteNode, left);
			} else {
				deleteRedUnLeaf(toDeleteNode, left);
			}
		} else {
			if (toDeleteNode.getLeft() == null
					&& toDeleteNode.getRight() == null)
				deleteBlackLeaf(toDeleteNode, left);
			else {
				if (toDeleteNode.getLeft() != null
						&& toDeleteNode.getRight() != null)
					deleteBlackWithTwoChildren(toDeleteNode, left);
				else
					deleteBlackWithAChild(toDeleteNode, left);
			}
		}

		showTreeByWidth(root);
		System.out.println("==========================");
	}

	public void deleteRedLeaf(Node toDeleteNode, boolean left) {
		Node father = toDeleteNode.getFather();
		if (left) {
			father.setLeft(null);
		} else {
			father.setRight(null);
		}
		if (toDeleteNode.getBrother() != null)
			toDeleteNode.getBrother().setBrother(null);
	}

	public void deleteRedUnLeaf(Node toDeleteNode, boolean left) {
		Node replace = toDeleteNode.getLeft();
		while (replace.getRight() != null)
			replace = replace.getRight();
		// toDeleteNode.setColor(replace.getColor());
		toDeleteNode.setValue(replace.getValue());
		if (replace.getColor() == Node.RED)
			deleteRedLeaf(replace, false);
		else {
			if (toDeleteNode.getLeft() == replace)
				deleteBlackWithAChild(replace, true);
			else
				deleteBlackWithAChild(replace, false);
		}
	}

	public void deleteBlackLeaf(Node toDeleteNode, boolean left) {
		// 因为红黑树的平衡限制，黑色节点不可能没有兄弟
		deleteBlackWithAChild(toDeleteNode, left);
	}

	public void deleteBlackWithTwoChildren(Node toDeleteNode, boolean left) {
		Node replace = toDeleteNode.getLeft();
		while (replace.getRight() != null)
			replace = replace.getRight();
		// toDeleteNode.setColor(replace.getColor());
		toDeleteNode.setValue(replace.getValue());
		if (replace.getColor() == Node.RED)
			deleteRedLeaf(replace, false);
		else {
			if (replace.getLeft() == null)
				deleteBlackLeaf(toDeleteNode, left);
			else
				deleteBlackWithAChild(toDeleteNode, left);
		}
	}

	public void deleteBlackWithAChild(Node toDeleteNode, boolean left) {
		Node father = toDeleteNode.getFather();
		Node brother = toDeleteNode.getBrother();
		Node son = null;
		if (toDeleteNode.getLeft() != null)
			son = toDeleteNode.getLeft();
		else
			son = toDeleteNode.getRight();

		if (left) {
			father.setLeft(son);
		} else
			father.setRight(son);
		if (son != null) {
			son.setFather(father);
			son.setBrother(brother);
			son.setColor(toDeleteNode.getColor());
		}
		Node node = son;
		// 因为当前节点为黑色，所以兄弟节点一定存在，所以不需要判断
		if (brother.getColor() == Node.RED) {
			// 红兄问题
			adjustDelBWithRBrother(father, brother, node, left);
		} else {
			// 黑兄问题
			adjustDelBWithBBrother(father, brother, node, left);
		}

	}

	public void adjustDelBWithRBrother(Node father, Node brother, Node node,
			boolean lefet) {
		// 红兄问题
		brother.setFather(father.getFather());
		brother.setBrother(father.getBrother());
		brother.setColor(Node.BLACK);
		father.setFather(brother);
		father.setColor(Node.RED);
		if (father.getLeft() == node) {
			// 往左转
			father.setRight(brother.getLeft());
			father.setBrother(brother.getRight());
			if (father.getBrother() != null)
				father.getBrother().setBrother(father);
			if (node != null)
				node.setBrother(father.getRight());
			if (father.getRight() != null)
				father.getRight().setBrother(node);
			brother.setLeft(father);
		} else {
			// 往右转
			father.setLeft(brother.getRight());
			father.setRight(brother.getLeft());
			if (father.getBrother() != null)
				father.getBrother().setBrother(father);
			if (node != null)
				node.setBrother(father.getLeft());
			if (father.getLeft() != null)
				father.getLeft().setBrother(node);
			brother.setRight(father);
		}
	}

	public void adjustDelBWithBBrother(Node father, Node brother, Node node,
			boolean left) {
		// 黑兄问题
		Node leftSon = brother.getLeft();
		Node rightSon = brother.getRight();
		/*
		 * if ((leftSon == null && rightSon == null) || (leftSon.getColor() ==
		 * Node.BLACK && rightSon.getColor() == Node.BLACK)) { // 黑兄二黑侄问题
		 * adjustDelBWithBBrotherTowBSons(father, brother, node, left); } else {
		 * if ((leftSon.getColor() == Node.RED && rightSon == null) ||
		 * (leftSon.getColor() == Node.RED && rightSon.getColor() ==
		 * Node.BLACK)) { // 黑兄左红侄右黑侄 adjustDelBWithBBrotherLeftRRightB(father,
		 * brother, node, left); } else { // 黑兄右红侄 if (brother.getRight() !=
		 * null && brother.getRight().getColor() == Node.RED)
		 * adjustDelBWithBBrotherRightR(father, brother, node, left); } }
		 */

		// 双黑侄
		if (leftSon == null && rightSon == null)
			adjustDelBWithBBrotherTowBSons(father, brother, node, left);
		if (leftSon == null && rightSon != null
				&& rightSon.getColor() == Node.BLACK)
			adjustDelBWithBBrotherTowBSons(father, brother, node, left);
		if (rightSon == null && leftSon != null
				&& leftSon.getColor() == Node.BLACK)
			adjustDelBWithBBrotherTowBSons(father, brother, node, left);
		if (leftSon != null && leftSon.getColor() == Node.BLACK
				&& rightSon != null && rightSon.getColor() == Node.BLACK)
			adjustDelBWithBBrotherTowBSons(father, brother, node, left);

		if (leftSon != null) {
			if (leftSon.getColor() == Node.RED)
				if (rightSon == null || rightSon.getColor() == Node.BLACK)
					// 左红侄右黑侄
					adjustDelBWithBBrotherLeftRRightB(father, brother, node,
							left);
		}
		if (rightSon != null)
			if (rightSon.getColor() == Node.RED)
				// 右红侄子
				adjustDelBWithBBrotherRightR(father, brother, node, left);

	}

	public void adjustDelBWithBBrotherTowBSons(Node father, Node brother,
			Node node, boolean left) {
		// 黑兄二黑侄问题
		father.setColor(Node.BLACK);
		brother.setColor(Node.RED);
	}

	public void adjustDelBWithBBrotherLeftRRightB(Node father, Node brother,
			Node node, boolean left) {
		// 黑兄左红侄子右黑侄子
		Node blNode = brother.getLeft();
		if (blNode.getLeft() != null)
			blNode.getLeft().setBrother(brother);
		brother.setColor(Node.RED);
		brother.setFather(blNode);
		brother.setLeft(blNode.getRight());
		brother.setBrother(blNode.getLeft());
		if (brother.getLeft() != null) {
			brother.getLeft().setFather(brother);
			brother.getLeft().setBrother(brother.getRight());
		}
		brother.getRight().setBrother(brother.getLeft());
		if (father.getLeft() == node) {
			father.setRight(blNode);
		} else {
			father.setLeft(blNode);
		}
		blNode.setColor(Node.BLACK);
		blNode.setFather(father);
		blNode.setRight(father);
		blNode.setBrother(node);
	}

	public void adjustDelBWithBBrotherRightR(Node father, Node brother,
			Node node, boolean left) {
		// 黑兄右红侄
		if (brother.getRight().getColor() == Node.RED) {
			Node grandFather = father.getFather();
			if (grandFather.getLeft() == father)
				grandFather.setLeft(brother);
			else
				grandFather.setRight(brother);
			brother.setBrother(father.getBrother());
			brother.setColor(father.getColor());
			father.setColor(Node.BLACK);
			father.setFather(brother);
			brother.getRight().setColor(Node.BLACK);
			if (father.getLeft() == node) {
				father.setRight(brother.getLeft());
				brother.setLeft(father);
				father.setBrother(brother.getRight());
				brother.getRight().setBrother(father);
				if (node != null)
					node.setBrother(father.getRight());
				if (father.getRight() != null) {
					father.getRight().setFather(father);
					father.getRight().setBrother(node);
				}
			} else {
				father.setLeft(brother.getRight());
				brother.setRight(father);
				father.setBrother(brother.getLeft());
				if (brother.getLeft() != null)
					brother.getLeft().setBrother(father);
				if (node != null)
					node.setBrother(father.getLeft());
				father.getLeft().setFather(father);
				father.getLeft().setBrother(node);
			}
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
				if (node.getColor() == Node.RED)
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
		Node father = node.getFather();
		Node grandFather = father.getFather();
		Node greatGrandFather = grandFather.getFather();
		grandFather.setColor(Node.RED);
		if (greatGrandFather == null) {
			root = node.getFather();
		} else {
			if (greatGrandFather.getLeft() == grandFather) {
				greatGrandFather.setLeft(node.getFather());
			} else
				greatGrandFather.setRight(node.getFather());
			father.setBrother(grandFather.getBrother());
		}
		father.setFather(greatGrandFather);
		father.setRight(grandFather);
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
		Node father = node.getFather();
		Node grandFather = father.getFather();
		Node greatGrandFather = grandFather.getFather();
		grandFather.setColor(Node.RED);
		if (greatGrandFather == null) {
			root = node.getFather();
		} else {
			if (greatGrandFather.getLeft() == grandFather)
				greatGrandFather.setLeft(node.getFather());
			else
				greatGrandFather.setRight(node.getFather());
			father.setBrother(grandFather.getBrother());
		}
		father.setFather(greatGrandFather);
		father.setLeft(grandFather);
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
		RedBlackTree2 redBlackTree = new RedBlackTree2(values);
		redBlackTree.deleteNode(5);
	}

}
