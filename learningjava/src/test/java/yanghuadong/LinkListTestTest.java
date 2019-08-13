package yanghuadong;

import org.junit.Before;
import org.junit.Test;
import yanghuadong.LinkListTest.*;


import static org.junit.Assert.*;

public class LinkListTestTest {
	private  Node head;
	private  Node one;
	private  Node two;
	private  Node three;
	private  Node four;

	@Before
	public void data() {
		head = new Node(0);
		one = new Node(1);
		two = new Node(2);
		three = new Node(3);
		four = new Node(4);

		head.setNext(one);
		one.setNext(two);
		two.setNext(three);
		three.setNext(four);
	}

	@Test
	public void reverseLinkedList() {
		printUsage(head);
		Node newHead = LinkListTest.reverseLinkedList(head);
		assertEquals(four, newHead);
		System.out.println();
		printUsage(newHead);
	}

	@Test
	public void reverseWithCycle() {
		printUsage(head);
		Node newHead = LinkListTest.reverseWithCycle(head);
		assertEquals(four, newHead);
		System.out.println();
		printUsage(newHead);
	}

	@Test
	public void theKthNode() {
		assertEquals(two,LinkListTest.theKthNode(head,2));
		assertEquals(four,LinkListTest.theKthNode(head,0));
		assertEquals(head,LinkListTest.theKthNode(head,4));
		assertEquals(null,LinkListTest.theKthNode(head,5));
		assertEquals(null,LinkListTest.theKthNode(head,6));
		assertEquals(null,LinkListTest.theKthNode(head,-2));
	}

	@Test
	public void theMiddleNode() {
		assertEquals(two, LinkListTest.theMiddleNode(head));
		assertEquals(two,LinkListTest.theMiddleNode(one));
		assertEquals(three,LinkListTest.theMiddleNode(three));
		assertEquals(four,LinkListTest.theMiddleNode(four));
	}

	@Test
	public void hasCircle() {
		assertEquals(false, LinkListTest.hasCircle(head));
		assertEquals(false, LinkListTest.hasCircle(three));

		four.setNext(two);
		assertEquals(true, LinkListTest.hasCircle(head));
	}

	@Test
	public void findLoopPort() {
		assertEquals(null, LinkListTest.findLoopPort(four));
		assertEquals(null, LinkListTest.findLoopPort(head));
		four.setNext(two);
		assertEquals(two, LinkListTest.findLoopPort(head));
		assertEquals(two, LinkListTest.findLoopPort(two));
	}

	@Test
	public void getNodeLength() {
		assertEquals(5,LinkListTest.getNodeLength(head));
		assertEquals(1,LinkListTest.getNodeLength(four));
		assertEquals(0,LinkListTest.getNodeLength(null));
	}

	private void printUsage(Node head) {
		while (head != null) {
			System.out.print(head);
			head = head.getNext();
		}
	}
}