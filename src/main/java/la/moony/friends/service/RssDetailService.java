package la.moony.friends.service;

import la.moony.friends.vo.RssDetail;
import reactor.core.publisher.Mono;

public interface RssDetailService {

    Mono<RssDetail> fetchRssDetail(String rssUrl,int fetchLimitNumber);
}
