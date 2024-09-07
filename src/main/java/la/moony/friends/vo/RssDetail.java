package la.moony.friends.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
public class RssDetail {

    private Channel channel;

    @Data
    public static class Channel {

        private String title;

        private String link;

        private String description;

        private List<Item> items;
    }

    @Data
    public static class Item {
        private String title;

        private String link;

        private String author;

        private String description;

        private String uri;

        private Instant pubDate;

    }

}
