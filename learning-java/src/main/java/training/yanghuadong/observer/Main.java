package training.yanghuadong.observer;

/**
 * @author BirdSnail
 * @date 2020/3/17
 */
public class Main {

    public static void main(String[] args) {
        PCLNewsAgency observable = new PCLNewsAgency();
        PCLNewsChannel observer = new PCLNewsChannel();
        observable.addPropertyChangeListener(observer);

        int i = 0;
        while (true) {
            observable.setNews("hello" + i++);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
