package training.yanghuadong.observer;

import java.util.*;

/**
 * @author BirdSnail
 * @date 2020/3/25
 */
public class RadioStation {
  EventBus eventBus;

    public RadioStation(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void publish(String channel, String message) {
        eventBus.publish(channel, message);
    }
}


interface Listener{
    void subscribe(String channel);

    void onMessage(String message);
}

class EventBus{

    Map<String, List<Listener>> listeners = new HashMap<>();

    public void subscribe(String channel, Listener listener) {
        final List<Listener> oldListeners = listeners.getOrDefault(channel, new ArrayList<>());
        oldListeners.add(listener);
        listeners.put(channel, oldListeners);
    }

    public void publish(String channel, String message) {
        listeners.getOrDefault(channel, Collections.emptyList()).forEach(listener -> listener.onMessage(message));
    }

}

class Car implements Listener{

    EventBus eventBus;

    public Car(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void subscribe(String channel) {
        eventBus.subscribe(channel, this);
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }
}
