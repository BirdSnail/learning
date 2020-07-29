package training.yanghuadong.observer;

/**
 * @author BirdSnail
 * @date 2020/3/17
 */
public class Main {

    public static void main(String[] args) {
//        PCLtest();
        subscribeAndPublish();
    }

    private static void PCLtest() {
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

    public static void subscribeAndPublish() {
        EventBus eventBus = new EventBus();
        final RadioStation radioStation = new RadioStation(eventBus);
        final Car car = new Car(eventBus);
        String channel = "happy end";
        car.subscribe(channel);

        for (int i = 0; i < 10; i++) {
            radioStation.publish(channel, "hello" + i);
        }
    }

}
