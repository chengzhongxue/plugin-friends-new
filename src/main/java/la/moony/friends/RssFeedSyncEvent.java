package la.moony.friends;

import la.moony.friends.extension.Link;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.List;

@Getter
public class RssFeedSyncEvent extends ApplicationEvent {
    private final Link link;
    private final int sum;
    private final List<String> disableSyncList;

    public RssFeedSyncEvent(Object source, Link link, int sum, List<String> disableSyncList) {
        super(source);
        this.link = link;
        this.sum = sum;
        this.disableSyncList = disableSyncList;
    }
}
