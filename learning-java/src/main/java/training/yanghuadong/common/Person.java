package training.yanghuadong.common;

import com.sun.javafx.sg.prism.web.NGWebView;

import java.util.Arrays;

/**
 * @author: Mr.Yang
 * @create: 2019-09-24
 */
public class Person {

	public Integer id;
	public String name;

	public static void print(int[] a) {
		System.out.println("111111");
		System.out.println(Arrays.toString(a));
	}

	public Person(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Person() {

		System.out.println("33333333333");
	}

	static {
		System.out.println("222222222222");
	}

	public static void main(String[] args) {
		new Person();
		new Person();
	}



}
