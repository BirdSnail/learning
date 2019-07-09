package graph.util;

import lombok.ToString;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * @author: Mr.Yang
 * @create: 2019-07-08
 */
public class EnumTrianglesData {
	/**
	 * 一个图中的边,包含两个顶点
	 */
	public static class Edge extends Tuple2<Integer, Integer> {
		private static final long serialVersionUID = 1L;

		// 元素下标
		public static final int V1 = 0;
		public static final int V2 = 1;

		public Edge() {
		}

		public Edge(final Integer firstVertex, final Integer secondVertex) {
			super(firstVertex, secondVertex);
		}

		public void setFirstVertex(final int firstVertex) {
			this.setField(firstVertex, V1);
		}

		public void setSecondVertex(final int secondVertex) {
			this.setField(secondVertex, V2);
		}

		public int getFirstVertex() {
			return this.getField(V1);
		}

		public int getSecondVertex() {
			return this.getField(V2);
		}

		@Override
		public String toString() {
			return super.toString();
		}
	}

	/**
	 * 三角形的三个顶点
	 */
	public static class Triade extends Tuple3<Integer, Integer, Integer> {
		// 元素下标
		public static final int T1 = 0;
		public static final int T2 = 1;
		public static final int T3 = 2;

		public Triade() {

		}

		public void setT1(final Integer firstVertex) {
			this.setField(firstVertex, T1);
		}

		public void setT2(final Integer secondVertex) {
			this.setField(secondVertex, T2);
		}

		public void setT3(final Integer threeVertex) {
			this.setField(threeVertex, T3);
		}

		public int getT1() {
			return this.getField(T1);
		}

		public int getT2() {
			return this.getField(T2);
		}

		public int getT3() {
			return this.getField(T3);
		}

		@Override
		public String toString() {
			return super.toString();
		}
	}
}
