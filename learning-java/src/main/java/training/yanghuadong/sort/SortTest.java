package training.yanghuadong.sort;

import java.util.Arrays;

/**
 * 经典排序七大算法
 *
 * @author: Mr.Yang
 * @create: 2019-08-08
 */
public class SortTest {

	public static void main(String[] args) {
		int[] dataArray = new int[]{2, 122, 52, 4, 52, 32, 54, 222, 20, 13, 98, 78, 54, 23, 30, 10, 95, 56, 84, 76, 61, 23};
		System.out.println("==============================================================\n" +
				"unsort:" + Arrays.toString(dataArray));

//		bubbleSort(dataArray);
//		selectSort(dataArray);
//		insertSort(dataArray);
//		mergeSort(dataArray);
//		quackSort(dataArray);
		heapSort(dataArray);
	}


	/**
	 * 冒泡排序
	 */
	public static void bubbleSort(int[] array) {
		int counter = 0;

		for (int i = 0; i < array.length - 1; i++) {

			for (int j = 0; j + 1 < array.length - i; j++) {
				if (array[j] > array[j + 1]) {
					int tmp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = tmp;

					counter++;
				}
			}
		}
		printUsge(array, counter);
	}

	/**
	 * 选择排序
	 */
	public static void selectSort(int[] array) {
		int counter = 0;

		for (int i = 0; i < array.length - 1; i++) {

			for (int j = i + 1; j < array.length; j++) {
				if (array[i] > array[j]) {
					int tmp = array[i];
					array[i] = array[j];
					array[j] = tmp;

					counter++;
				}
			}
		}
		printUsge(array, counter);
	}

	/**
	 * 插入排序
	 */
	public static void insertSort(int[] array) {
		int counter = 0;

		for (int i = 1; i < array.length; i++) {
			if (array[i] < array[i - 1]) {
				int tmp = array[i];
				int index = i;

				// 内循环，待插入元素与已排序的元素组比较
				// [2, 52, 122, 4] --> [2,52,122,122] -->[2, 52, 52, 122] -->[2, 4, 52, 122]
				for (int j = i - 1; j >= 0; j--) {
					if (tmp < array[j]) {
						array[j + 1] = array[j];
						index = j;

						counter++;
					} else {
						break;
					}
				}

				array[index] = tmp;
			}
		}
		printUsge(array, counter);
	}

	/**
	 * 归并排序
	 */
	public static void mergeSort(int[] array) {
		mergeSort(array, 0, array.length - 1);
		printUsge(array);
	}

	private static void mergeSort(int[] array, int start, int end) {
		if (start < end) {
			int mid = (start + end) / 2;
			mergeSort(array, start, mid);
			mergeSort(array, mid + 1, end);
			merge(array, start, mid, end);
		}
	}

	/**
	 * 快速排序
	 */
	public static void quackSort(int[] array) {
		quackSort(array, 0, array.length - 1);
		printUsge(array);
	}

	private static void quackSort(int[] array, int left, int right) {
		if (left < right) {

			int key = array[left];
			int i = left;
			int j = right;

			while (i < j) {
				// 若右边的元素比基准元素大，就向左移一步
				while (array[j] >= key && i < j) {
					j--;
				}

				// 若左边的元素比基准元素小，就向右移一步
				while (array[i] <= key && i < j) {
					i++;
				}

				if (i < j) {
					int tmp = array[i];
					array[i] = array[j];
					array[j] = tmp;
				}
			}

			// i所在的下标就是基准元素应该出现的位置
			array[left] = array[i];
			array[i] = key;

			quackSort(array, left, i - 1);
			quackSort(array, i + 1, right);
		}
	}

	/**
	 * 推排序
	 */
	public static void heapSort(int[] array) {
		// 构建一个大顶堆
		int first = array.length / 2 - 1;
		for (int i = first; i >= 0; i--) {
			Heapify(array, array.length - 1, i);
		}

		// 大根推转换成有序数组
		for (int i = array.length - 1; i > 0; i--) {
			int temp = array[i];
			array[i] = array[0];
			array[0] = temp;

			Heapify(array, i - 1, 0);
		}

		printUsge(array);
	}

	/**
	 * 保持非叶子节点是最大值
	 * 		max
	 * 		/ \
	 * 	   a   b
	 * 基础知识：
	 * 	1. 在二叉树中，一个下标为i的节点它的左子节点下标 (2*i + 1),右子节点：(2*i +2)
	 *
	 * @param array 数组
	 * @param maxIndex 推的边界
	 * @param position  当前节点位置
	 */
	private static void Heapify(int[] array, int maxIndex, int position) {

		while (true) {
			// 子节点索引
			int child = 2 * position + 1;

			if (child > maxIndex) {
				break;
			}

			// 将子节点索引指向最大值所在的位置
			if ((child + 1) <= maxIndex && array[child] < array[child + 1]) {
				child += 1;
			}

			// 交换父子节点值，较大的子节点成为父节点
			if (array[child] > array[position]) {
				int temp = array[position];
				array[position] = array[child];
				array[child] = temp;

				position = child;
			} else {
				break;
			}
		}
	}

	private static void merge(int[] origin, int left, int mid, int right) {
		// 保留排好序的结果
		int[] result = new int[right - left + 1];

		int l_start = left;
		int r_start = mid + 1;

		// 将left到right这中间的部分排序
		for (int index = 0; index < result.length; index++) {
			if (l_start <= mid && r_start <= right) {
				if (origin[l_start] < origin[r_start]) {
					result[index] = origin[l_start];
					l_start++;
				} else {
					result[index] = origin[r_start];
					r_start++;
				}
			} else if (l_start > mid) {
				result[index] = origin[r_start];
				r_start++;
			} else if (r_start > right) {
				result[index] = origin[l_start];
				l_start++;
			}
		}

		// 将排好序的部分替换到原先的数组中
		for (int i = 0; i < result.length; i++) {
			origin[left + i] = result[i];
		}
	}

	/**
	 * 合并连个有序的数组
	 */
	private static int[] mergeOfTwoArray(int[] left, int[] right) {
		int[] result = new int[left.length + right.length];

		int l_start = 0;
		int r_start = 0;

		for (int index = 0; index < result.length; index++) {
			if (l_start < left.length && r_start < right.length) {

				if (left[l_start] < right[r_start]) {
					result[index] = left[l_start];
					l_start++;
				} else {
					result[index] = right[r_start];
					r_start++;
				}
			}
			// 左数组已经遍历完，直接将右数组剩下的元素追加到结果数组中
			else if (l_start >= left.length) {
				result[index] = right[r_start];
				r_start++;
			}
			// 右数组已经遍历完，直接将左数组剩下的元素追加到结果数组中
			else if (r_start >= right.length) {
				result[index] = left[l_start];
				l_start++;
			}
		}

		return result;
	}


	private static void printUsge(int[] array) {
		System.out.println("sort:" + Arrays.toString(array));
		System.out.println("==============================================================");
	}

	private static void printUsge(int[] array, int count) {
		System.out.println("sort:" + Arrays.toString(array));
		System.out.println("交换数据次数：" + count);
		System.out.println("==============================================================");
	}
}
