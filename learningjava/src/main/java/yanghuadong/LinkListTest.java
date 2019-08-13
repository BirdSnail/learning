package yanghuadong;

import lombok.Data;

import java.time.temporal.Temporal;

/**
 * @author: Mr.Yang
 * @create: 2019-08-12
 */
public class LinkListTest {


	/**递归方式反转链表*/
	public static Node reverseLinkedList(Node head) {
		if (head == null || head.next == null) {
			return head;
		}

		Node newHead = reverseLinkedList(head.next);
		head.next.next = head;
		head.next = null;

		return newHead;
	}

	/**遍历方式反转链表*/
	public static Node reverseWithCycle(Node head) {
		Node pre = null;
		while (head != null) {
			Node next = head.next;

			head.next = pre;
			pre = head;
			head = next;
		}

		return pre;
	}

	/**求单向链表倒数第 K 个节点*/
	public static Node theKthNode(Node head, int k) {
		if (k < 0) return null;

		Node result = head;
		// 先走k步
		for (int i = 0; i < k; i++) {
			if (head == null || head.next == null){
				return null;
			}
			head = head.next;
		}
		// 到达末尾
		while (head.next != null) {
			head = head.next;
			result = result.next;
		}

		return result;
	}

	/**获取单向链表中间的节点*/
	public static Node theMiddleNode(Node head) {
		Node midNode = head;

		while (head != null) {
			// 第一个指针先走两步
			for (int i = 0; i < 2; i++) {
				if (head == null || head.next == null) {
					return midNode;
				}
				head = head.next;
			}

			// 第二个指针走一步
			midNode = midNode.next;
		}
		return midNode;
	}

	/**判断单链表中是否存在环*/
	public static boolean hasCircle(Node head) {
		boolean result = false;
		if (head.next == null || head == null) {
			return result;
		}

		Node slow = head;

		while (head != null && head.next !=null) {
			head = head.next.next;
			slow = slow.next;

			if (head == slow) {
				return true;
			}
		}

		return result;
	}

	/**找到环的入口点*/
	public static Node findLoopPort(Node head) {
		if (head.next == null || head == null) {
			return null; // 链表为null或只有一个节点
		}

		Node entrance = head;
		Node slow = head;

		while (head.next != null && head !=null) {
			head = head.next.next;
			slow = slow.next;

			if (head == slow) {
				break;
			}
		}

		// 不存在环
		if (head != slow) return null;

		// 两者相遇的地方就是环的入口
		while (entrance != slow) {
			entrance = entrance.next;
			slow = slow.next;
		}

		return entrance;
	}

	/**两个单链表是否相交*/
	public static boolean isIntersect(Node first, Node second) {
		if (first == null || second == null) {
			return false;
		}

		while (first.next != null) {
			first = first.next;
		}

		while (second.next != null) {
			second = second.next;
		}

		if (first == second) {
			return true;
		} else {
			return false;
		}
	}

	/**两个有环的单链表是否相交*/
	public static boolean isIntersectWithLoop(Node first, Node second) {
		if (!hasCircle(first) || !hasCircle(second)) {
			return false; // 不是有环链表
		}

		Node firstPalce = findMeetingPlace(first);
		Node secondPlace = findMeetingPlace(second);

		// 环形链表循环
		Node temp = firstPalce.next;
		while (temp != firstPalce) {
			if (temp == secondPlace) {
				return true;
			}
			temp = temp.next;
		}

		return false;
	}

	private static Node findMeetingPlace(Node node) {
		if (!hasCircle(node)) return null;

		Node slow = node;
		while (node.next != null && node !=null) {
			node = node.next.next;
			slow = slow.next;

			if (node == slow) {
				break;
			}
		}

		return slow;
	}

	/**两个相交的有环单链表第一个公共节点*/
	public static Node findFirstIntersectNode(Node left, Node right) {
		int leftLength = getNodeLength(left);
		int rightLength = getNodeLength(right);

		// 对齐两个链表
		if (leftLength > rightLength) {
			for (int i = 0; i < leftLength - rightLength; i++) {
				left = left.next;
			}
		} else {
			for (int i = 0; i < rightLength - leftLength; i++) {
				right = right.next;
			}
		}

		while (left != null) {
			if (left == right) {
				return left;
			}
			left = left.next;
			right = right.next;
		}

		return null;
	}

	public static int getNodeLength(Node node) {
		int count = 0;

		while (node != null) {
			node = node.next;
			count++;
		}

		return count;
	}

	@Data
	public static class Node {
		private int data;
		private Node next;

		public Node(int data) {
			this.data = data;
		}

		@Override
		public String toString() {
			return data + "-->";
		}
	}

}
