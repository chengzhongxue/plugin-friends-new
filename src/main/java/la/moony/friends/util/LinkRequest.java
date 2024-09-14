package la.moony.friends.util;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkRequest {

    private static final Logger log = LoggerFactory.getLogger(LinkRequest.class);

    private final static String USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
            + "Chrome/58.0.3029.110 Safari/537.3";

    public static String getLinkRss(String linkUrl) {
        Document document = null;
        try {
            document = Jsoup.connect(linkUrl)
                .followRedirects(true)
                .maxBodySize(1024 * 1024 * 20)
                .timeout(10000)
                .headers(
                    Map.of(
                        "User-Agent", USER_AGENT,
                        "Referer", linkUrl,
                        "Accept", "text/html,application/xhtml+xml,application/xml"
                    )
                )
                .get();
        } catch (IOException e) {
            log.error("Failed to get link rss ",e);
        }

        if(document != null) {
            Element rssElement = document.selectFirst("link[type=application/rss+xml]");
            if (Objects.nonNull(rssElement)) {
                return rssElement.absUrl("href");
            }

            Element atomElement = document.selectFirst("link[type=application/atom+xml]");
            if (Objects.nonNull(atomElement)) {
                return atomElement.absUrl("href");
            }
        }

        return "";
    }
}
