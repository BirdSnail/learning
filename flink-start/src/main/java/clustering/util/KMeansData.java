package clustering.util;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: Mr.Yang
 * @create: 2019-07-15
 */
public class KMeansData {

	// We have the data as object arrays so that we can also generate Scala Data Sources from it.
	public static final Object[][] CENTROIDS = new Object[][] {
			new Object[] {1, -31.85, -44.77},
			new Object[]{2, 35.16, 17.46},
			new Object[]{3, -5.16, 21.93},
			new Object[]{4, -24.06, 6.81}
	};

	public static final Object[][] POINTS = new Object[][] {
			new Object[] {-14.22, -48.01},
			new Object[] {-22.78, 37.10},
			new Object[] {56.18, -42.99},
			new Object[] {35.04, 50.29},
			new Object[] {-9.53, -46.26},
			new Object[] {-34.35, 48.25},
			new Object[] {55.82, -57.49},
			new Object[] {21.03, 54.64},
			new Object[] {-13.63, -42.26},
			new Object[] {-36.57, 32.63},
			new Object[] {50.65, -52.40},
			new Object[] {24.48, 34.04},
			new Object[] {-2.69, -36.02},
			new Object[] {-38.80, 36.58},
			new Object[] {24.00, -53.74},
			new Object[] {32.41, 24.96},
			new Object[] {-4.32, -56.92},
			new Object[] {-22.68, 29.42},
			new Object[] {59.02, -39.56},
			new Object[] {24.47, 45.07},
			new Object[] {5.23, -41.20},
			new Object[] {-23.00, 38.15},
			new Object[] {44.55, -51.50},
			new Object[] {14.62, 59.06},
			new Object[] {7.41, -56.05},
			new Object[] {-26.63, 28.97},
			new Object[] {47.37, -44.72},
			new Object[] {29.07, 51.06},
			new Object[] {0.59, -31.89},
			new Object[] {-39.09, 20.78},
			new Object[] {42.97, -48.98},
			new Object[] {34.36, 49.08},
			new Object[] {-21.91, -49.01},
			new Object[] {-46.68, 46.04},
			new Object[] {48.52, -43.67},
			new Object[] {30.05, 49.25},
			new Object[] {4.03, -43.56},
			new Object[] {-37.85, 41.72},
			new Object[] {38.24, -48.32},
			new Object[] {20.83, 57.85}
	};

	public static DataSet<Centroid> getDefaultCentroidDataSet(ExecutionEnvironment env) {
		List<Centroid> centroidList = new LinkedList<Centroid>();
		for (Object[] centroid : CENTROIDS) {
			centroidList.add(
					new Centroid((Integer) centroid[0], (Double) centroid[1], (Double) centroid[2]));
		}
		return env.fromCollection(centroidList);
	}

	public static DataSet<Point> getDefaultPointDataSet(ExecutionEnvironment env) {
		List<Point> pointList = new LinkedList<Point>();
		for (Object[] point : POINTS) {
			pointList.add(new Point((Double) point[0], (Double) point[1]));
		}
		return env.fromCollection(pointList);
	}

	/**
	 * A simple two-dimensional point.
	 */
	public static class Point implements Serializable {

		public double x, y;

		public Point() {}

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public Point add(Point other) {
			x += other.x;
			y += other.y;
			return this;
		}

		public Point div(long val) {
			x /= val;
			y /= val;
			return this;
		}

		public double euclideanDistance(Point other) {
			return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
		}

		public void clear() {
			x = y = 0.0;
		}

		@Override
		public String toString() {
			return x + " " + y;
		}
	}

	/**
	 * A simple two-dimensional centroid, basically a point with an ID.
	 */
	public static class Centroid extends Point {

		public int id;

		public Centroid() {}

		public Centroid(int id, double x, double y) {
			super(x, y);
			this.id = id;
		}

		public Centroid(int id, Point p) {
			super(p.x, p.y);
			this.id = id;
		}

		@Override
		public String toString() {
			return id + " " + super.toString();
		}
	}
}
