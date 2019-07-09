package graph;


import graph.util.EnumTrianglesData.*;

import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.*;

/**
 * 三角枚举算法
 * 在一推图中找出三角形。在单个三角形这中图中，一个顶点有两条边。因此找出图中一个共享顶点是否只少含有两条边，
 * 若有，判断两个边的子顶点组成的边是否存在。
 * example：
 * 2
 * / \
 * 3   4
 * 如果边（2-3），（2-4）存在，只需要判断边（3-4）是否存在即可。
 *
 * @author: Mr.Yang
 * @create: 2019-07-08
 */
public class EnumTriangles {

	private static Edge[] edges = {new Edge(1, 2),
			new Edge(1, 3),
			new Edge(1, 4),
			new Edge(1, 5),
			new Edge(2, 3),
			new Edge(2, 5),
			new Edge(3, 4),
			new Edge(3, 7),
			new Edge(3, 8),
			new Edge(5, 6),
			new Edge(7, 8)};


	public static void main(String[] args) throws Exception {
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1);

		DataSet<Edge> flipEdge = env.fromElements(edges)
				.map(new FlipEdgeMapFunction());

		// 一个共享顶点下面的所有边
		flipEdge.groupBy(Edge.V1)
				.sortGroup(Edge.V2, Order.ASCENDING)
				.reduceGroup(new TriadBuilder())
				// 过滤掉原始图中不存在的边
				.join(flipEdge).where(Triade.T2, Triade.T3).equalTo(Edge.V1, Edge.V2)
				.with(new TriadeFilter())
				.print();


	}

	//**********************************************************************************
	// UTIL FUNCTION
	//**********************************************************************************

	/**
	 * 翻转特定的边，因为（2-3）（3-2）可视为同一条边
	 */
	private static class FlipEdgeMapFunction implements MapFunction<Edge, Edge> {

		@Override
		public Edge map(Edge value) throws Exception {
			if (value.getFirstVertex() > value.getSecondVertex()) {
				final int tmp = value.getFirstVertex();
				value.setFirstVertex(value.getSecondVertex());
				value.setSecondVertex(tmp);
			}
			return value;
		}
	}

	/**
	 * 通过边的共享顶点构建三角形的三个顶点
	 *    d1
	 *   /
	 * s1--d2       s1是共享顶点
	 *  \
	 *   \
	 *    d3
	 */
	private static class TriadBuilder implements GroupReduceFunction<Edge, Triade> {
		private Triade outTriade = new Triade();
		private List<Integer> vertices = new ArrayList<>();

		@Override
		public void reduce(Iterable<Edge> values, Collector<Triade> out) throws Exception {
			final Iterator<Edge> iterator = values.iterator();

			vertices.clear();

			// 第一条最小的边
			Edge minVertex = iterator.next();
			// 共享顶点
			outTriade.setT1(minVertex.getFirstVertex());
			vertices.add(minVertex.getSecondVertex());

			while (iterator.hasNext()) {
				Edge higherEdge = iterator.next();

				for (Integer vertex : vertices) {
					outTriade.setT2(vertex);
					outTriade.setT3(higherEdge.getSecondVertex());
					out.collect(outTriade);
				}

				// 遍历完成后需要添加到顶点列表中，进行下一条边的遍历
				vertices.add(higherEdge.getSecondVertex());
			}
		}
	}

	/**
	 * 过滤不存在的边
	 */
	private static class TriadeFilter implements JoinFunction<Triade, Edge, Triade> {

		@Override
		public Triade join(Triade first, Edge second) throws Exception {
			return first;
		}
	}
}
