package yanghuadong;

import java.util.Arrays;

/**
 * 全排列与组合
 * @author: Mr.Yang
 * @create: 2019-08-15
 */
public class PermuAndComb {

	public static void main(String[] args) {
		char[] chars = {'a', 'b','c'};
		permutation(chars, 0, chars.length);
	}

	/**                [a  b  c]
	 *                 /   |   \
	 *                a    b    c                       第0层
	 *               /     |     \
	 *            [b,c]   [a,c]  [a,b]
	 *            /   \   /   \   / \
	 *           b     c         a   b                  第1层
	 *          |       \        |    \
	 *         [c]       [b]    [b]   [a]
	 *         |          \      |      \
	 *         c           b     b       a              第2层
	 *
	 * 递归与回溯
	 * @param s 输入的字符数组
	 * @param k 表示层，代表第几个数
	 * @param length 字符数组长度
	 */
	public static void permutation(char[] s, int k, int length) {
		if (k == length - 1) {
			System.out.println(Arrays.toString(s));
			return;
		}

		for (int i = k; i < length; i++) {
			char temp = s[i];
			s[i] = s[k];
			s[k] = temp;

			permutation(s, k+1, length);

			temp = s[i];
			s[i] = s[k];
			s[k] = temp;
		}
	}
}
