package training.yanghuadong.observer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * 被观察的对象
 * 主动将消息发送给观察者
 *
 * @author BirdSnail
 * @date 2020/3/17
 */
public class PCLNewsAgency {

    private String news;
    private PropertyChangeSupport support;

    public PCLNewsAgency() {
        this.support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void setNews(String value) {
        support.firePropertyChange("name", news, value);
        this.news = value;
    }

}
