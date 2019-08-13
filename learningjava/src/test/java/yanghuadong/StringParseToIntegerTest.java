package yanghuadong;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringParseToIntegerTest {

	@Test
	public void parseInt() throws Exception {
		assertEquals(Integer.MIN_VALUE, StringParseToInteger.parseInt(String.valueOf(Integer.MIN_VALUE)));
		assertEquals(Integer.MAX_VALUE, StringParseToInteger.parseInt(String.valueOf(Integer.MAX_VALUE)));
		assertEquals(1235, StringParseToInteger.parseInt("1235"));
		assertEquals(-1235, StringParseToInteger.parseInt("-1235"));
	}

	@Test(expected = Exception.class)
	public void parseIntOverflow() throws Exception {
		StringParseToInteger.parseInt("1235648000000000000");
	}
}