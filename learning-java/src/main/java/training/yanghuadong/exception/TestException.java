package training.yanghuadong.exception;

import java.io.IOException;

/**
 * 验证抛出非受检异常会不会终端循环
 * --会，操作程序退出
 * @author BirdSnail
 * @date 2020/2/26
 */
public class TestException {

    static int i = 0;

    public static void main(String[] args) throws IOException {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i++ == 5) {
//                throw new RuntimeException();
                throw new IOException();
            }
            System.out.println(i);
        }
    }
}
