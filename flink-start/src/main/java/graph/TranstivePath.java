package graph;

import graph.util.TranstivePathData;
import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.GroupReduceIterator;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.operators.CoGroupOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.GroupReduceOperator;
import org.apache.flink.api.java.operators.IterativeDataSet;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.*;

/**
 * @author: Mr.Yang
 * @create: 2019-07-11
 */
public class TranstivePath {

	private static final int maxIterations = 10;

	public static void main(String[] args) throws Exception {
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1);

		final DataSet<Tuple2<Long, Long>> edges = TranstivePathData.getDefaultEdgeDataSet(env);

		// 原始路径的迭代头
		IterativeDataSet<Tuple2<Long, Long>> paths = edges.iterate(maxIterations);
		// 迭代逻辑
		DataSet<Tuple2<Long, Long>> nextPath = paths.join(edges).where(1).equalTo(0)
//				.reduceGroup(new GroupReduceFunction<Tuple2<Tuple2<Long, Long>, Tuple2<Long, Long>>, Tuple2<Long, Long>>() {
//					@Override
//					public void reduce(Iterable<Tuple2<Tuple2<Long, Long>, Tuple2<Long, Long>>> values, Collector<Tuple2<Long, Long>> out)
//							throws Exception {
//						for (Tuple2<Tuple2<Long, Long>, Tuple2<Long, Long>> value : values) {
//							out.collect(Tuple2.of(value.f0.f0, value.f1.f1));
//						}
//					}
//				})
				.with(new BuildNewPath())
				// 转发字段，优化flink性能（正确的语义可以生成更高效的执行计划，但错误的语义可能导致计算结果的错误，小心使用）
				.withForwardedFieldsFirst("0").withForwardedFieldsSecond("1")
				.union(paths)
				.groupBy(0, 1)
				.reduceGroup(new DistinctPath())
				.withForwardedFields("0;1");

		// newPath为空时就终止迭代
		DataSet<Tuple2<Long, Long>> newPath = paths.coGroup(nextPath).where(0).equalTo(0)
				.with(new TerminationOfIteration())
				.withForwardedFieldsFirst("0").withForwardedFieldsSecond("0");

		paths.closeWith(nextPath, newPath)
				// 二次排序
				.sortPartition(new KeySelector<Tuple2<Long, Long>, Tuple2<Long, Long>>() {
					@Override
					public Tuple2<Long, Long> getKey(Tuple2<Long, Long> value) throws Exception {
						return new Tuple2<Long, Long>(value.f0, value.f1);
					}
				}, Order.ASCENDING)
//				.sortPartition(0, Order.ASCENDING)
//				.sortPartition(1,Order.ASCENDING)
				.print();

	}

	//************************************************************************************************************
	// USER FUNCTION
	//************************************************************************************************************

	/**
	 * left:(1, 2)
	 * right:(2, 3)
	 * out:(1, 3) 生成一个可以连通的新路径
	 */
	private static class BuildNewPath implements JoinFunction<Tuple2<Long, Long>, Tuple2<Long, Long>, Tuple2<Long, Long>> {

		@Override
		public Tuple2<Long, Long> join(Tuple2<Long, Long> first, Tuple2<Long, Long> second) throws Exception {
			return Tuple2.of(first.f0, second.f1);
		}
	}

	/**
	 * 去除与原始数据集union后重复的元素
	 */
	private static class DistinctPath implements GroupReduceFunction<Tuple2<Long, Long>, Tuple2<Long, Long>> {

		@Override
		public void reduce(Iterable<Tuple2<Long, Long>> values, Collector<Tuple2<Long, Long>> out) throws Exception {
			out.collect(values.iterator().next());
		}
	}

	/**
	 * 不再生成新的path时可以终止迭代
	 */
	private static class TerminationOfIteration implements CoGroupFunction<Tuple2<Long, Long>, Tuple2<Long, Long>,
			Tuple2<Long, Long>> {

		private Set<Tuple2<Long, Long>> preSet = new HashSet<>();

		@Override
		public void coGroup(Iterable<Tuple2<Long, Long>> first, Iterable<Tuple2<Long, Long>> second,
		                    Collector<Tuple2<Long, Long>> out) throws Exception {

			for (Tuple2<Long, Long> prePath : first) {
				preSet.add(prePath);
			}

			// 不包含就代表产生了新path
			for (Tuple2<Long, Long> nextPath : second) {
				if (!preSet.contains(nextPath)) {
					out.collect(nextPath);
				}
			}
		}
	}
}
