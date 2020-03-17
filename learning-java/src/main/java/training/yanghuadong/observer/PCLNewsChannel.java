package training.yanghuadong.observer;

import lombok.Data;
import lombok.Getter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author BirdSnail
 * @date 2020/3/17
 */
@Getter
public class PCLNewsChannel  implements PropertyChangeListener {

    private String news;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.news = (String) evt.getNewValue();
        System.out.println(news);
    }
}
