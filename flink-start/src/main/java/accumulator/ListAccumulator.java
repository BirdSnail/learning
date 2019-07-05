package accumulator;

import org.apache.flink.api.common.accumulators.Accumulator;
import org.apache.flink.api.java.tuple.Tuple3;

import java.util.ArrayList;

/**
 * 一个list的累加器，存放每个位置对应的空字段数量
 * <p>
 * arraylist= {0,1,0}.代表第二个index位置的空字段元素个数为1，其余位置为0.
 * <p/>
 *
 * @author: Mr.Yang
 * @create: 2019-07-05
 */
public class ListAccumulator implements Accumulator<Integer, ArrayList<Integer>> {

	private static final long serialVersionUID = 1L;

	private ArrayList<Integer> resultList;

	public ListAccumulator() {
		this.resultList = new ArrayList<>();
	}

	public ListAccumulator(ArrayList<Integer> resultList) {
		this.resultList = resultList;
	}

	@Override
	public void add(Integer position) {
		updateResultList(position,1);
	}

	@Override
	public ArrayList<Integer> getLocalValue() {
		return this.resultList;
	}

	@Override
	public void resetLocal() {
		this.resultList.clear();
	}

	/**
	 * 运行在多个task中的累加器进行合并，统计出整个Job的结果
	 * @param other
	 */
	@Override
	public void merge(Accumulator<Integer, ArrayList<Integer>> other) {
		final ArrayList<Integer> otherList = other.getLocalValue();
		for (int i = 0; i < otherList.size(); i++) {
			updateResultList(i, otherList.get(i));
		}
	}

	@Override
	public Accumulator<Integer, ArrayList<Integer>> clone() {
		return new ListAccumulator(new ArrayList<Integer>(resultList));
	}

	/**
	 * 更新累加器的值
	 *
	 * @param position 出现empty field的index位置。
	 */
	private void updateResultList(Integer position, int delta) {
		while (resultList.size() <= position) {
			this.resultList.add(0);
		}

		// 考虑到多个task并行的情况，一个task示例取出数值的时候另一个task示例会对值进行修改
		// 因此用一个final的中间变量保证计算的正确性
		final int compent = resultList.get(position);
		resultList.set(position, compent + delta);
	}
}
