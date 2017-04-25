package balanceTree;

import sortedTree.SortedTree;
import node.Node;

public class BalanceTree {
	/*
	 * @author Wang 平衡二叉树AVL，只会添加非重复值，重复值不保存
	 */

	static SortedTree tree = new SortedTree();
	Node root = null;

	public BalanceTree() {
	}

	public BalanceTree(int[] values) {
		for (int i = 0; i < values.length; i++) {
			addNode(values[i]);
		}
	}

	public void deleteValue(int value) {
		deleteNode(new Node(value));
	}

	public void deleteNode(Node node) {
		if (node == null || root == null)
			return;
		if (node.getValue() == root.getValue() && getHeight(root) == 1) {
			root = null;
			return;
		}
		Node toDeleteNode = getNode(node.getValue());
		if (toDeleteNode == null)
			return;
		Node father = toDeleteNode.getFather();
		boolean left = true;
		boolean right = !left;
		if (father != null) {
			if (node.getValue() > father.getValue()) {
				right = true;
				left = !right;
			}
		} else {
			toDeleteNode = root;
		}
		if (toDeleteNode.getLeft() == null && toDeleteNode.getRight() == null) {
			deleteLeaf(toDeleteNode);
		} else {
			if (toDeleteNode.getLeft() == null
					|| toDeleteNode.getRight() == null) {
				if (toDeleteNode.getLeft() == null) {
					// 左子树为空
					deleteNodeWithoutLeft(toDeleteNode, left);
				} else {
					// 右子树为空
					deleteNodeWithoutRight(toDeleteNode, left);
				}
			} else {
				// 左右子树都不为空
				deleteNodeWithBoth(toDeleteNode);
			}
		}

		if (father != null)
			adjustTreeToBalance(father);

		tree.showTreeByWidth(root);
		System.out.println("===============================");
	}

	public void deleteNodeWithBoth(Node toDeleteNode) {
		// 删除一个左右子树都不为空的节点
		Node replace = toDeleteNode.getLeft();
		while (replace.getRight() != null)
			replace = replace.getRight();
		toDeleteNode.setValue(replace.getValue());
		if (replace.getLeft() == null)
			deleteLeaf(replace);
		else
			deleteNodeWithoutRight(replace, false);
	}

	public void deleteLeaf(Node toDeleteNode) {
		// 删除叶子节点
		Node father = toDeleteNode.getFather();
		if (father == null) {
			root = null;
			return;
		}
		if (toDeleteNode.getValue() > father.getValue()) {
			father.setRight(null);
			toDeleteNode.setFather(null);
		} else {
			father.setLeft(null);
			toDeleteNode.setFather(null);
		}
	}

	public void deleteNodeWithoutLeft(Node toDeleteNode, boolean left) {
		// 删除左子树为空的节点
		Node father = toDeleteNode.getFather();
		if (father == null) {
			root = toDeleteNode.getRight();
			root.setFather(null);
			return;
		}
		toDeleteNode.getRight().setFather(father);
		if (left) {
			father.setLeft(toDeleteNode.getRight());
		} else {
			father.setRight(toDeleteNode.getRight());
		}
	}

	public void deleteNodeWithoutRight(Node toDeleteNode, boolean left) {
		// 删除右子树为空的节点
		Node father = toDeleteNode.getFather();
		if (father == null) {
			root = toDeleteNode.getLeft();
			root.setFather(null);
			return;
		}
		toDeleteNode.getLeft().setFather(father);
		if (left)
			father.setLeft(toDeleteNode.getLeft());
		else
			father.setRight(toDeleteNode.getLeft());
	}

	public boolean contains(int value) {
		Node node = getNode(value);
		if (node != null)
			return true;
		else
			return false;
	}

	public Node getNode(int value) {
		/*
		 * 如果查询value在数中，返回节点引用，否则返回null
		 */
		if (root == null)
			return null;
		Node node = root;
		while (true) {
			if (node.getValue() != value) {
				if (value > node.getValue()) {
					if (node.getRight() != null) {
						node = node.getRight();
					} else {
						node = null;
						break;
					}
				} else {
					if (node.getLeft() != null) {
						node = node.getLeft();
					} else {
						node = null;
						break;
					}
				}
			} else {
				break;
			}

		}
		return node;
	}

	public Node toAddPostionFather(Node node) {
		Node parent = root;
		if (parent != null)
			while (true) {
				if (node.getValue() > parent.getValue()) {
					if (parent.getRight() != null) {
						parent = parent.getRight();
					} else
						break;
				} else {
					if (node.getValue() < parent.getValue()) {
						if (parent.getLeft() != null) {
							parent = parent.getLeft();
						} else
							break;
					} else {
						parent = null;
						break;
					}

				}
			}

		return parent;
	}

	public void addNode(int value) {
		Node node = new Node(value);
		if (root == null) {
			root = node;
			return;
		}

		if (contains(value))
			return;

		Node parent = toAddPostionFather(node);
		if (parent == null)
			return;

		if (node.getValue() > parent.getValue())
			parent.setRight(node);
		else
			parent.setLeft(node);

		node.setFather(parent);

		// 以上完成的是每个节点按照二叉树的顺序插入

		adjustTreeToBalance(node);

		tree.showTreeByWidth(root);
		System.out.println("====================================");

	}

	public void adjustTreeToBalance(Node node) {
		Node unBalancedNode = findUnbalancedNode(node);

		if (unBalancedNode != null) {
			if (getDifHeight(unBalancedNode) > 0) {
				if (getDifHeight(unBalancedNode.getLeft()) < 0) {
					// LR
					LRRound(unBalancedNode);
				} else {
					// LL；判断何种不平衡出错了。
					LLRound(unBalancedNode);
				}
			} else {
				if (getDifHeight(unBalancedNode.getRight()) < 0) {
					// RR
					RRRound(unBalancedNode);
				} else {
					// RL
					RLRound(unBalancedNode);
				}
			}
		}
	}

	public void LRRound(Node unBalancedNode) {
		Node unFather = unBalancedNode.getFather();
		Node lNode = unBalancedNode.getLeft();
		Node lrNode = lNode.getRight();
		lrNode.setFather(unFather);
		if (unFather == null)
			root = lrNode;
		else {
			if (unFather.getLeft() == unBalancedNode) {
				unFather.setLeft(lrNode);
			} else {
				unFather.setRight(lrNode);
			}
		}
		if(lrNode.getLeft() != null)
			lrNode.getLeft().setFather(lNode);
		lNode.setRight(lrNode.getLeft());
		if(lrNode.getRight() != null)
			lrNode.getRight().setFather(unBalancedNode);
		unBalancedNode.setLeft(lrNode.getRight());
		lrNode.setRight(unBalancedNode);
		unBalancedNode.setFather(lrNode);
		lrNode.setLeft(lNode);
		lNode.setFather(lrNode);
	}

	public void LLRound(Node unBalancedNode) {
		Node unFather = unBalancedNode.getFather();
		Node lNode = unBalancedNode.getLeft();
		Node lrNode = lNode.getRight();
		lNode.setFather(unFather);
		unBalancedNode.setLeft(null);
		if (lrNode != null) {
			lrNode.setFather(unBalancedNode);
		}
		if (unFather == null)
			root = lNode;
		else {
			if (unFather.getLeft() == unBalancedNode) {
				unFather.setLeft(lNode);
			} else {
				unFather.setRight(lNode);
			}
		}
		lNode.setRight(unBalancedNode);
		unBalancedNode.setFather(lNode);
		unBalancedNode.setLeft(lrNode);
	}

	public void RRRound(Node unBalancedNode) {
		Node unFather = unBalancedNode.getFather();
		Node rNode = unBalancedNode.getRight();
		Node rlNode = rNode.getLeft();
		unBalancedNode.setRight(null);
		rNode.setFather(unFather);
		if (rlNode != null)
			rlNode.setFather(unBalancedNode);
		if (unFather == null)
			root = rNode;
		else {
			if (unFather.getLeft() == unBalancedNode)
				unFather.setLeft(rNode);
			else
				unFather.setRight(rNode);
		}
		rNode.setLeft(unBalancedNode);
		unBalancedNode.setFather(rNode);
		unBalancedNode.setRight(rlNode);
	}

	public void RLRound(Node unBalancedNode) {
		Node unFather = unBalancedNode.getFather();
		Node rNode = unBalancedNode.getRight();
		Node rlNode = rNode.getLeft();
		rlNode.setFather(unFather);
		if (unFather == null)
			root = rlNode;
		else {
			if (unFather.getLeft() == unBalancedNode) {
				unFather.setLeft(rlNode);
			} else {
				unFather.setRight(rlNode);
			}
		}
		if(rlNode.getLeft() != null)
			rlNode.getLeft().setFather(unBalancedNode);
		unBalancedNode.setRight(rlNode.getLeft());
		if(rlNode.getRight() != null)
			rlNode.getRight().setFather(rNode);
		rNode.setLeft(rlNode.getRight());
		rlNode.setLeft(unBalancedNode);
		unBalancedNode.setFather(rlNode);
		rlNode.setRight(rNode);
		rNode.setFather(rlNode);
	}

	public Node findUnbalancedNode(Node node) {
		if (node == null)
			return null;
		
		Node ret = node;
		while (true) {
			if (isBalacedTree(ret)) {
				if (ret.getFather() != null)
					ret = ret.getFather();
				else
					return null;
			} else
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
		// int[] values = { 8, 6, 11, 5, 4, 7, 9, 12 };
		// Node root = tree.buildSortedTree(values);
		// int height = balanceTree.getHeight(root);
//		int[] values = { 16, 3, 7, 11, 9, 26, 18, 26, 14, 15 };
		int[] values = {8,6,12,4,10,16,17};
		BalanceTree balanceTree = new BalanceTree(values);
		balanceTree.deleteValue(4);
		// balanceTree.deleteValue(15);
		// balanceTree.deleteValue(11);
		// balanceTree.deleteValue(18);
		balanceTree.deleteValue(6);
		// balanceTree.addNode(27);
	}

}
