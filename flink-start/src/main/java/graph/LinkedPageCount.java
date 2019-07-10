package graph;

import graph.util.PageRankData;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

/**
 * 计算每个page的链入个数
 *
 * @author: Mr.Yang
 * @create: 2019-07-09
 */
public class LinkedPageCount {

	public static void main(String[] args) throws Exception {
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		final DataSet<Tuple2<Long, Long>> edges = PageRankData.getDefaultEdgeDataSet(env);

		edges.groupBy(0)
				.reduceGroup(new BuildOutgoingEdgeArray())
				.flatMap(new PageTokenizer())
				.groupBy(0)
				.sum(1)
				.print();

	}

	//*************************************************************************************************
	// USERFUNCTION
	//*************************************************************************************************
	/**
	 * 找出一个page所有的链接的pageId
	 */
	private static class BuildOutgoingEdgeArray implements GroupReduceFunction<Tuple2<Long, Long>, Tuple2<Long, Long[]>> {

		private List<Long> Vertices = new ArrayList();

		@Override
		public void reduce(Iterable<Tuple2<Long, Long>> values, Collector<Tuple2<Long, Long[]>> out) throws Exception {
			Long pageId = 0L;
			Vertices.clear();

			for (Tuple2<Long, Long> edge : values) {
				pageId = edge.f0;
				Vertices.add(edge.f1);
			}

			out.collect(Tuple2.of(pageId, Vertices.toArray(new Long[Vertices.size()])));
		}
	}

	/**
	 * 为有链入的page计数，初始化为 1
	 */
	private static class PageTokenizer implements FlatMapFunction<Tuple2<Long, Long[]>, Tuple2<Long, Integer>> {

		private Tuple2<Long, Integer> pageOfCount = new Tuple2<>();

		@Override
		public void flatMap(Tuple2<Long, Long[]> value, Collector<Tuple2<Long, Integer>> out) throws Exception {
			Long[] commingPages = value.f1;
			for (Long page : commingPages) {
				pageOfCount.setField(page, 0);
				pageOfCount.setField(1, 1);
				out.collect(pageOfCount);
			}
		}
	}
}
