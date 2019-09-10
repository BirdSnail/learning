package training.yanghuadong.Collection;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author: Mr.Yang
 * @create: 2019-09-05
 */
public class LinkedListTest {

	public static void main(String[] args) {
		LinkedList<Integer> integers = getLinkedListInstance();

		Iterator<Integer> ite = integers.iterator();
		while (ite.hasNext()) {
			System.out.println(ite.next());
		}


		System.out.println("================================");


		for (Integer integer : integers) {
			System.out.println(integer);
		}
	}

	public static LinkedList<Integer> getLinkedListInstance() {
		LinkedList<Integer> linkedList = new LinkedList<>();
		for (int i = 0; i < 10; i++) {
			linkedList.add(i);
		}

		return linkedList;
	}
}
