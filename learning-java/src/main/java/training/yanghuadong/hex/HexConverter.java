package training.yanghuadong.hex;

/**
 * 任意进制转换
 *
 * @author BirdSnail
 * @date 2020/7/29
 */
public class HexConverter {

	public static void main(String[] args) {
		String num = "123";
		System.out.println(anyHexConverter(num, Hex.SIXTEEN, Hex.TWO));
		System.out.println(anyHexConverter("11", Hex.TEN, Hex.TEN));
	}

	/**
	 * 任意进制转换成十进制
	 *
	 * @param str    src value
	 * @param srcHex 原始进制
	 * @return 十进制的值
	 */
	static int converterToTen(String str, Hex srcHex) {
		int result = 0;
		int pre = 1;
		for (int i = str.length() - 1; i >= 0; i--) {
			char c = str.charAt(i);
			if (c >= 'A') {
				result += (c - 'A' + 10) * pre;
			} else {
				result += (c - '0') * pre;
			}
			pre *= srcHex.getNum();
		}
		return result;
	}

	/**
	 * 十进制数转换成任意进制
	 *
	 * @param num 十进制数
	 * @param hex 目标进制
	 * @return 转换后的值
	 */
	static String tenConverter(int num, Hex hex) {
		return converterFromTen(num, hex, "");
	}

	private static String converterFromTen(int num, Hex hex, String result) {
		int y = num % hex.getNum();
		int s = num / hex.getNum();

		if (y > 9) {
			result = (char) ('A' + y - 10) + result;
		} else {
			result = y + result;
		}

		if (s == 0) {
			return result;
		}
		return converterFromTen(s, hex, result);
	}

	/**
	 * 两种进制之间的任意转换
	 *
	 * @param value     原始值
	 * @param srcHex    原始进制
	 * @param targetHex 目标进制
	 * @return 值
	 */
	static Object anyHexConverter(String value, Hex srcHex, Hex targetHex) {
		int temp = converterToTen(value, srcHex);
		return tenConverter(temp, targetHex);
	}

	public static enum Hex {
		TWO(2),
		EIGHT(8),
		TEN(10),
		SIXTEEN(16);

		private int num;

		Hex(int num) {
			this.num = num;
		}

		public int getNum() {
			return num;
		}
	}
}
