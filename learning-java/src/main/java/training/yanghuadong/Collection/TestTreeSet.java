package training.yanghuadong.Collection;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author: Mr.Yang
 * @create: 2019-09-09
 */
public class TestTreeSet {

	public static void main(String[] args) {

		Set<String> treeset = new TreeSet<>();
		treeset.add("a");
		treeset.add("d");
		treeset.add("e");
		treeset.add("f");
		treeset.add("b");

		treeset.forEach(str -> System.out.println(str));
	}
}
