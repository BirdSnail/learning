package training.yanghuadong.Collection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Mr.Yang
 * @create: 2019-09-10
 */
public class TestHashMap {

	public static void main(String[] args) {
		Map<String, Integer> haspMap = new HashMap<>();
		haspMap.put("a", 1);
		haspMap.put("b", 1);
		haspMap.put("a", 1);

		int[] a = {1, 2, 3};
		int[] b = a;

		b[0]++;
		System.out.println(a[0]);

		for (String s : haspMap.keySet()) {

		}

		for (Map.Entry<String, Integer> entry : haspMap.entrySet()) {
			entry.getKey();
			entry.getValue();
		}
	}
}

