package graph.util;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: Mr.Yang
 * @create: 2019-07-11
 */
public class TranstivePathData {

	public static final Long[][] EDGES = new Long[][]{
			new Long[]{1L, 2L},
			new Long[]{2L, 3L},
			new Long[]{2L, 4L},
			new Long[]{3L, 5L},
			new Long[]{6L, 7L},
			new Long[]{8L, 9L},
			new Long[]{8L, 10L},
			new Long[]{5L, 11L},
			new Long[]{11L, 12L},
			new Long[]{10L, 13L},
			new Long[]{9L, 14L},
			new Long[]{13L, 14L},
			new Long[]{1L, 15L},
			new Long[]{16L, 1L}
	};

	public static DataSet<Tuple2<Long, Long>> getDefaultEdgeDataSet(ExecutionEnvironment env) {

		List<Tuple2<Long, Long>> edgeList = new LinkedList<>();
		for (Long[] edge : EDGES) {
			edgeList.add(Tuple2.of(edge[0], edge[1]));
		}
		return env.fromCollection(edgeList);
	}
}
