package training.yanghuadong.String;

/**
 * @author: Mr.Yang
 * @create: 2019-09-06
 */
public class TestString {


	public static void main(String[] args) {
		String s1 = "b";
		String s2 = "abcdfsabc";
		String s3 = "abcde";

//		System.out.println(s1.regionMatches(0, s2, 0, 3));
//		System.out.println(s1.regionMatches(0, s3, 3, 3));
//
//		System.out.println(s2.lastIndexOf(s1));
//
//		System.out.println(s1.lastIndexOf('c'));

		System.out.println(simulateStringIndexOf(s3.toCharArray(), s1.toCharArray(), 0));
	}

	/**模拟{@code String}中的在一个字符串中查找子字符串的索引的方法*/
	public static int simulateStringIndexOf(char[] source, char[] target, int fromIndex) {
		char first = target[0];

		int max = source.length - target.length;
		for (int i = fromIndex; i <= max; i++) {
			if (source[i] != first) {
				// 在source中找到第一个字符串的位置
				while (++i < max && source[i] != first);

			}

			if (i <= max ) {
				// 使用两个变量帮助比较
				int j = i + 1;
				int end = j + target.length - 1;
				int k = 1;
				while (j < end && source[j] == target[k]) {
					j++;
					k++;
				}

				// 成功走到了指定末尾
				if (j == end) {
					return i;
				}
			}
		}

		return -1;
	}

}
