package clustering;

import clustering.util.KMeansData;
import clustering.util.KMeansData.*;
import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.IterativeDataSet;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Flink实现K-聚类算法
 *
 * @author: Mr.Yang
 * @create: 2019-07-15
 */
public class KMeans {

	private static final String CENTROIDS_BROADCAST_NAME = "centroids";

	public static void main(String[] args) throws Exception {
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		DataSet<Point> points = KMeansData.getDefaultPointDataSet(env);
		DataSet<Centroid> centroids = KMeansData.getDefaultCentroidDataSet(env);

		IterativeDataSet<Centroid> loop = centroids.iterate(10);

		DataSet<Centroid> newCentroids = points.map(new SelectNearestCenter())
				// 将迭代流广播
				.withBroadcastSet(loop, CENTROIDS_BROADCAST_NAME)
				.groupBy(0)
				//.reduce(new CentroidAccumulator())
				.reduceGroup(new GroupReduceFunction<Tuple3<Integer, Point, Integer>, Centroid>() {
					@Override
					public void reduce(Iterable<Tuple3<Integer, Point, Integer>> values, Collector<Centroid> out)
							throws Exception {
						Integer count = 1;

						Iterator<Tuple3<Integer, Point, Integer>> ite = values.iterator();
						Tuple3<Integer, Point, Integer> tuple3 = ite.next();

						Point p = tuple3.f1;
						while (ite.hasNext()) {
							p.add(ite.next().f1);
							count++;
						}

						out.collect(new Centroid(tuple3.f0, p.div(count)));
					}
				});
		//.map(new CentroidAverager());

		//最终的中心点
		DataSet<Centroid> finalCentroid = loop.closeWith(newCentroids);

		points.map(new SelectNearestCenter()).withBroadcastSet(finalCentroid, CENTROIDS_BROADCAST_NAME)
				.setParallelism(1)
				.sortPartition(0, Order.ASCENDING)
				.print();
	}

	//***************************************************************************************
	// USER FUNCTION
	//***************************************************************************************

	/**
	 * 找出每个点所属的区域
	 */
	private static class SelectNearestCenter extends RichMapFunction<Point, Tuple3<Integer, Point, Integer>> {

		private Collection<Centroid> broadcast;

		@Override
		public void open(Configuration parameters) throws Exception {
			broadcast = getRuntimeContext().getBroadcastVariable(CENTROIDS_BROADCAST_NAME);
		}

		@Override
		public Tuple3<Integer, Point, Integer> map(Point value) throws Exception {
			Double minDistance = Double.MAX_VALUE;
			Integer centerId = -1;

			// 计算与所有中心点的距离
			for (Centroid centroid : broadcast) {
				double distance = value.euclideanDistance(centroid);

				// 选取距离最短的中心点
				if (distance < minDistance) {
					minDistance = distance;
					centerId = centroid.id;
				}
			}
			return Tuple3.of(centerId, value, 1);
		}
	}

	/**
	 * 计算一个区域的所有点的总和
	 */
	private static class CentroidAccumulator implements ReduceFunction<Tuple3<Integer, Point, Integer>> {

		@Override
		public Tuple3<Integer, Point, Integer> reduce(Tuple3<Integer, Point, Integer> value1,
		                                              Tuple3<Integer, Point, Integer> value2) throws Exception {
			return Tuple3.of(value1.f0, value1.f1.add(value2.f1), value1.f2 + value2.f2);
		}
	}

	/**
	 * 新的center
	 */
	private static class CentroidAverager implements MapFunction<Tuple3<Integer, Point, Integer>, Centroid> {

		@Override
		public Centroid map(Tuple3<Integer, Point, Integer> value) throws Exception {
			return new Centroid(value.f0, value.f1.div(value.f2));
		}
	}
}
