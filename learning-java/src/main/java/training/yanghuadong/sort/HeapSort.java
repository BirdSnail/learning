package training.yanghuadong.sort;

import java.util.Arrays;

/**
 * 推排序：找出第k个最大的元素
 * <p>
 * 1. 创建一个大顶堆
 * 2. 不断删除堆顶元素，之后重新构建大顶堆
 * 3. 删除k-1次后留在堆顶的元素
 *
 * @author BirdSnail
 * @date 2020/7/29
 */
public class HeapSort {
	static int[] arrays = {2, 25, 34, 3, 5, 65, 12, 9};

	public static void main(String[] args) {
		System.out.println(findKthNum(arrays, 3));
		System.out.println(Arrays.toString(arrays));
	}

	static int findKthNum(int[] arrays, int k) {
		maxHeap(arrays);
		/**
		 * 堆顶是最大值，取出最大值重新排序
		 */
		int heapSize = arrays.length;
		for (int i = heapSize - 1; i >= arrays.length - k + 1; i--) {
			swap(arrays, 0, i);
			heapSize--;
			maxHeapify(arrays, 0, heapSize);
		}
		return arrays[0];
	}

	/**
	 * 构建指定大小的大顶堆
	 *
	 * @param arrays 数组
	 */
	static void maxHeap(int[] arrays) {
		//遍历每一个非叶子节点
		int start = arrays.length / 2 - 1;
		for (int i = start; i >= 0; i--) {
			maxHeapify(arrays, i, arrays.length);
		}
	}

	static void maxHeapify(int[] arrays, int position, int heapSize) {
		if (position >= heapSize) {
			return;
		}
		int child = 2 * position + 1;
		if ((child + 1) < heapSize && arrays[child] < arrays[child + 1]) {
			child += 1;
		}

		if (child < heapSize && arrays[child] > arrays[position]) {
			swap(arrays, child, position);
			maxHeapify(arrays, child, heapSize);
		}
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param arrays 数组
	 * @param i      index i
	 * @param j      index j
	 */
	static void swap(int[] arrays, int i, int j) {
		if (i >= arrays.length || j >= arrays.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		int temp = arrays[i];
		arrays[i] = arrays[j];
		arrays[j] = temp;
	}

}
