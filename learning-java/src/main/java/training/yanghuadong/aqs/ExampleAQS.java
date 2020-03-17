package training.yanghuadong.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author BirdSnail
 * @date 2020/2/26
 */
public class ExampleAQS {

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock();

//        new Thread(() ->{
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            lock.lock();
//        }).start();

//        lock.lock();
//        lock.lock();

        CountDownLatch cdl = new CountDownLatch(1);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cdl.countDown();
        }).start();

        cdl.await();
    }
}
