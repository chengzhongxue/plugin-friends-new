package la.moony.friends.service.impl;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import la.moony.friends.service.RssDetailService;
import la.moony.friends.vo.RssDetail;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class RssDetailServiceImpl implements RssDetailService {


    @Override
    public Mono<RssDetail> fetchRssDetail(String rssUrl, int fetchLimitNumber) {
        RssDetail rssDetail = new RssDetail();
        try {
            // 创建 SyndFeedInput 对象并从 URL 获取 RSS 提要
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(rssUrl)));
            RssDetail.Channel channel = new RssDetail.Channel();
            channel.setTitle(feed.getTitle());
            channel.setLink(feed.getLink());
            channel.setDescription(feed.getDescription());
            rssDetail.setChannel(channel);
            List<RssDetail.Item> items = new ArrayList<>();
            int count = 0;
            for (SyndEntry syndEntry : feed.getEntries()) {
                RssDetail.Item item = new RssDetail.Item();
                item.setTitle(syndEntry.getTitle());
                item.setLink(syndEntry.getLink());
                item.setAuthor(syndEntry.getAuthor());
                item.setDescription(syndEntry.getDescription().getValue());
                item.setPubDate(syndEntry.getPublishedDate().toInstant());
                items.add(item);
                count++;
                if (count >= fetchLimitNumber) {
                    break;
                }
            }
            channel.setItems(items);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Mono.just(rssDetail);
    }

}
