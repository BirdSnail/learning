package training.yanghuadong.nio;

import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * @author BirdSnail
 * @date 2019/12/4
 */
public class BuffTest {

    public static void main(String[] args) {
        // 默认为写模式
        IntBuffer intBuffer = IntBuffer.allocate(10);
        intBuffer.get();
        intBuffer.get();
        intBuffer.get();
        intBuffer.get();

        intBuffer.put(1);
        intBuffer.put(2);
        intBuffer.put(3);
//        System.out.println(intBuffer.position());

        intBuffer.flip();
        System.out.println(intBuffer.get());
        System.out.println(intBuffer.get());
        System.out.println(intBuffer.position());
        // 剩余元素复制到数组开头，将position设置为最后一个没有读取的元素的位置
        System.out.println(intBuffer.compact());

        intBuffer.flip();
        int[] des = new int[intBuffer.limit()];
        intBuffer.get(des);
        System.out.println(Arrays.toString(des));
    }
}
