package graph;

import graph.util.PageRankData;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.operators.IterativeDataSet;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

/**
 * 网页排名算法计算每个页面的PR值，PR越高代表该网页越重要。
 * 参照谷歌的“page-rank”
 *
 * @author: Mr.Yang
 * @create: 2019-07-10
 */
public class PageRank {
	private static final int iteratorCount = 10;
	private static final double DAMPENING_FACTOR = 0.85;
	private static final double EPSILON = 0.0001;

	public static void main(String[] args) throws Exception {
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1);

		final DataSet<Long> pages = PageRankData.getDefaultPagesDataSet(env);
		final DataSet<Tuple2<Long, Long>> linkedPage = PageRankData.getDefaultEdgeDataSet(env);

		int numPages = PageRankData.getNumberOfPages();

		// 每个页面的PR初始值
		DataSet<Tuple2<Long, Double>> pagesWithRank = pages.map(new MapFunction<Long, Tuple2<Long, Double>>() {
			private Tuple2<Long, Double> initRank = new Tuple2<>();
			private Double initPR = 1.0d / numPages;

			@Override
			public Tuple2<Long, Double> map(Long value) throws Exception {
				initRank.setField(value, 0);
				initRank.setField(initPR, 1);
				return initRank;
			}
		});

		// 一个点所有相邻的点
		DataSet<Tuple2<Long, Long[]>> adjacencyInput = linkedPage.groupBy(0)
				.reduceGroup(new BuildOutgoingPageId());

		// 迭代头
		IterativeDataSet<Tuple2<Long, Double>> iteration = pagesWithRank.iterate(iteratorCount);

		// 迭代计算的step函数，由一系列的join，map，aggragate算子构成
		DataSet<Tuple2<Long, Double>> newRank = iteration.join(adjacencyInput).where(0).equalTo(0)
				.flatMap(new JoinVertexWithEdge())
				.groupBy(0)
				.aggregate(Aggregations.SUM, 1)
				.map(new DampingRank(DAMPENING_FACTOR, numPages));

		// 指定迭代终止条件
		DataSet<Tuple2<Long, Double>> finalRank = iteration.closeWith(newRank,
				newRank.join(pagesWithRank).where(0).equalTo(0).filter(new EpsilonFilter()));

		finalRank.sortPartition(1, Order.DESCENDING).print();
	}

	//*******************************************************************************************
	// USER FUNCTION
	//*******************************************************************************************

	/**
	 * 找出一个顶点所有的链出点
	 */
	private static class BuildOutgoingPageId implements GroupReduceFunction<Tuple2<Long, Long>, Tuple2<Long, Long[]>> {
		private List<Long> pages = new ArrayList<>();
		private Long id = 0L;

		@Override
		public void reduce(Iterable<Tuple2<Long, Long>> edges, Collector<Tuple2<Long, Long[]>> out) throws Exception {
			pages.clear();

			for (Tuple2<Long, Long> n : edges) {
				id = n.f0;
				pages.add(n.f1);
			}

			out.collect(Tuple2.of(id, pages.toArray(new Long[pages.size()])));
		}
	}

	/**
	 * 计算每个链入的权重PR
	 * B
	 * /
	 * A---C      B,C,D分别占有A的1/3的PR值
	 * \
	 * D
	 */
	private static class JoinVertexWithEdge implements FlatMapFunction<Tuple2<Tuple2<Long, Double>, Tuple2<Long, Long[]>>,
			Tuple2<Long, Double>> {

		private Tuple2<Long, Double> result = new Tuple2<>();

		@Override
		public void flatMap(Tuple2<Tuple2<Long, Double>, Tuple2<Long, Long[]>> value,
		                    Collector<Tuple2<Long, Double>> out) throws Exception {
			Long[] neighborPage = value.f1.f1;
			Double newPR = value.f0.f1 / (neighborPage.length + 0.0d);

			for (Long p : neighborPage) {
				result.setField(p, 0);
				result.setField(newPR, 1);
				out.collect(result);
			}
		}
	}

	/**
	 * 加入阻尼系数后的新PR值
	 * 阻尼系数公式:PR(A)=(1-d)/N + d(PR(T1)/C(T1)+ ... +PR(Tn)/C(Tn))
	 */
	private static class DampingRank implements MapFunction<Tuple2<Long, Double>, Tuple2<Long, Double>> {

		private Double dampening;
		private Double randomJump;

		public DampingRank(Double dampening, Integer numberOfPages) {
			this.dampening = dampening;
			this.randomJump = (1.0 - dampening) / numberOfPages;
		}

		@Override
		public Tuple2<Long, Double> map(Tuple2<Long, Double> value) throws Exception {
			value.f1 = value.f1 * dampening + randomJump;
			return value;
		}
	}

	/**
	 * 过滤变动较小的值
	 */
	private static class EpsilonFilter implements FilterFunction<Tuple2<Tuple2<Long, Double>, Tuple2<Long, Double>>> {

		@Override
		public boolean filter(Tuple2<Tuple2<Long, Double>, Tuple2<Long, Double>> value) throws Exception {
			return Math.abs(value.f0.f1 - value.f1.f1) > EPSILON;
		}
	}
}
