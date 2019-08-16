package yanghuadong;

/**
 * 将字符串解析成整数
 * 考虑点：
 *  1. 输入的检验，输入是否为空，是否为null，字符代表的数字是否在区间[0-9]中
 *  2. 输入的字符串是否超过int有效范围【Integer.MIN,Integer.MAX】---------> 除法，取模
 *  3. 符号问题，是正数还是负数
 *  4. 如何转换成整数 ----------> 从左到右扫描整个字符串，扫描到当前字符时，把上一次结果 * 10 + 当前字符代表的数字
 *  5. 只有一个‘+’，‘-’的情况
 * @author: Mr.Yang
 * @create: 2019-08-12
 */
public class StringParseToInteger {

	public static int parseInt(String s) throws Exception {
		if (s == null || s.length() == 0) {
			throw new Exception("illegal number input");
		}
		int MIN_DIV = -(Integer.MIN_VALUE / 10);
		int MAX_DIV = Integer.MAX_VALUE / 10;
		int MAX = Integer.MAX_VALUE % 10;
		int MIN = -(Integer.MIN_VALUE % 10);
		int digit = s.charAt(0);
		int index = 0;
		int result = 0;
		int single = 1;

		// 当有符号时，初始的索引加一
		if (digit == '-' || digit == '+') {
			if (s.length() == 1) {
				throw new Exception("illegal number input");
			}
			if (digit == '-') {
				single = -1;
			}

			index++;
		}

		while (index < s.length()) {
			digit = s.charAt(index++) - '0';

			if (digit >= 0 && digit <= 9) {
				if (single > 0 && (result > MAX_DIV || (result == MAX_DIV && digit > MAX))) {
					throw new Exception("number overflow");
				}
				if (single < 0 && (result > MIN_DIV || (result == MIN_DIV && digit > MIN))) {
					throw new Exception("number overflow");
				}

				result = result * 10 + digit;
			} else {
				throw new Exception("illegal number input");
			}
		}

		return single > 0 ? result : -result;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(parseInt(String.valueOf(Integer.MIN_VALUE)));
		System.out.println(parseInt(String.valueOf(Integer.MAX_VALUE)));
		System.out.println(parseInt("-1235"));
		System.out.println(parseInt("1235"));
	}
}
