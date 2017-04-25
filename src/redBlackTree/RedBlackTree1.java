package redBlackTree;

import java.util.LinkedList;
import java.util.List;

import sortedTree.SortedTree;

public class RedBlackTree1 {
	
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

	public RedBlackTree1() {
	}

	public RedBlackTree1(int[] values) {
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

		if (toDeleteNode.getLeft() != null && toDeleteNode.getRight() != null) {
			Node replace = toDeleteNode.getLeft();
			while (replace.getRight() != null)
				replace = replace.getRight();
			// toDeleteNode.setColor(replace.getColor());
			toDeleteNode.setValue(replace.getValue());
			if (replace.getLeft() == null) {
				if (replace.getColor() == Node.BLACK)
					deleteBlackLeaf(replace, false);
				else
					deleteRedLeaf(replace, false);
			} else {
				// 因为红色节点不可能有单支黑色，所以，如果replace有左子树节点，那他一定是黑色的
				deleteBlackWithAChild(replace, false);
			}
		} else {
			if (toDeleteNode.getLeft() != null
					|| toDeleteNode.getRight() != null) {
				// 只有单支，所以一定是黑色的
				deleteBlackWithAChild(toDeleteNode, left);
			} else {
				if (toDeleteNode.getColor() == Node.BLACK) {
					deleteBlackLeaf(toDeleteNode, left);
				} else {
					deleteRedLeaf(toDeleteNode, left);
				}
			}
		}

	}

	public void deleteBlackLeaf(Node toDeleteNode, boolean left) {

	}

	public void deleteRedLeaf(Node toDeleteNode, boolean left) {
		Node father = toDeleteNode.getFather();
		if (left) {
			father.setLeft(null);
		} else {
			father.setRight(null);
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
		son.setFather(father);
		son.setBrother(brother);

		son.setColor(toDeleteNode.getColor());

		Node node = son;
		// 因为当前节点为黑色，所以兄弟节点一定存在，所以不需要判断
		if (node.getBrother().getColor() == Node.RED) {
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
				node.setBrother(father.getLeft());
				if (father.getLeft() != null)
					father.getLeft().setBrother(node);
				brother.setRight(father);
			}
		} else {
			// 黑兄问题
			if (brother.getLeft().getColor() == Node.BLACK
					&& brother.getRight().getColor() == Node.BLACK) {
				// 黑兄二黑侄问题
				father.setColor(Node.BLACK);
				node.getBrother().setColor(Node.RED);
			} else {
				if (brother.getLeft().getColor() == Node.RED
						&& brother.getRight().getColor() == Node.BLACK) {
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
				} else {
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
							brother.setLeft(father);
							father.setRight(brother.getLeft());
							father.setBrother(brother.getRight());
							brother.getRight().setBrother(father);
							node.setBrother(father.getRight());
							if (father.getRight() != null) {
								father.getRight().setFather(father);
								father.getRight().setBrother(node);
							}
						}else{
							brother.setRight(father);
							father.setLeft(brother.getRight());
							father.setBrother(brother.getLeft());
							if(brother.getLeft() != null)
								brother.getLeft().setBrother(father);
							node.setBrother(father.getLeft());
							node.setBrother(father.getLeft());
						}
					}
				}
			}
		}

	}

	public boolean isBalancedRedBlackTree(Node node) {
		if (node == null)
			return false;
		boolean ret = false;
		return ret;
	}

	public int getBlackHeight(Node node) {
		// if(node == null)
		// return 0;
		// if(node.getColor() == Node.BLACK)
		// return getBlackHeight(node.)
		return 0;
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
		RedBlackTree1 redBlackTree = new RedBlackTree1(values);
	}

}
