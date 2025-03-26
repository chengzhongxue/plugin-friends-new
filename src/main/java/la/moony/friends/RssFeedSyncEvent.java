package la.moony.friends;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RssFeedSyncEvent extends ApplicationEvent {
    private final String name;
    private final String s;

    public RssFeedSyncEvent(Object source, String name, String s) {
        super(source);
        this.name = name;
        this.s = s;
    }
}
