package la.moony.friends.service.impl;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import la.moony.friends.extension.CronFriendPost;
import la.moony.friends.extension.FriendPost;
import la.moony.friends.extension.Link;
import la.moony.friends.service.FriendPostService;
import la.moony.friends.util.CommonUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListOptions;
import run.halo.app.extension.Metadata;
import run.halo.app.extension.PageRequestImpl;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.extension.router.selector.FieldSelector;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;
import static run.halo.app.extension.MetadataUtil.nullSafeAnnotations;
import static run.halo.app.extension.index.query.QueryFactory.all;
import static run.halo.app.extension.index.query.QueryFactory.equal;

@Component
public class FriendPostServiceImpl implements FriendPostService {

    private final ReactiveExtensionClient client;

    public FriendPostServiceImpl(ReactiveExtensionClient client) {
        this.client = client;
    }

    @Override
    public Mono<Void> synchronizationFriend() {
        var listOptions = new ListOptions();
        listOptions.setFieldSelector(FieldSelector.of(all()));
        return client.listAll(Link.class, listOptions, defaultSort())
            .flatMap(link -> {
                var linkName = link.getMetadata().getName();
                var annotations = nullSafeAnnotations(link);
                var rssUri = annotations.get("rss_uri");
                if (StringUtils.isNotEmpty(rssUri)) {
                   return getSuccessfulRetainLimit()
                       .flatMap(sum-> Flux.fromIterable(fetchFriendPost(rssUri,sum))
                           .flatMap(rssPost -> {
                               var friendPostListOptions = new ListOptions();
                               friendPostListOptions.setFieldSelector(FieldSelector.of(
                                   equal("spec.postLink",rssPost.getSpec().getPostLink())
                               ));
                               return client.listBy(FriendPost.class, friendPostListOptions, PageRequestImpl.ofSize(1))
                                   .flatMap(friendPosts -> {
                                       long total = friendPosts.getTotal();
                                       if (total==0) {
                                           FriendPost friendPost = new FriendPost();
                                           // 设置元数据才能保存
                                           friendPost.setMetadata(new Metadata());
                                           friendPost.getMetadata().setGenerateName("friend-post-");
                                           friendPost.setSpec(rssPost.getSpec());
                                           var spec = friendPost.getSpec();
                                           spec.setLogo(link.getSpec().getLogo());
                                           spec.setAuthor(link.getSpec().getDisplayName());
                                           spec.setAuthorUrl(link.getSpec().getUrl());
                                           spec.setLinkName(linkName);
                                           String description = friendPost.getSpec().getDescription();
                                           //解析html内容转换成文本
                                           if (StringUtils.isNotEmpty(description)){
                                               description = CommonUtils.parseAndTruncateHtml2Text(description, 200);
                                               String regexp = "[　*|\\s*]*";
                                               description = description.replaceFirst(regexp, "").trim();
                                               spec.setDescription(description);
                                           }
                                           return client.create(friendPost).thenReturn(true);
                                       }
                                       return Mono.just(false);
                                   });
                           })
                           .then(delFriendPost(linkName,sum))
                       );
                }
                return Mono.just(false);
            }).then();
    }


    public Mono<Void> delFriendPost(String linkName,Integer sum) {
        var friendPostListOptions = new ListOptions();
        friendPostListOptions.setFieldSelector(FieldSelector.of(
            equal("spec.linkName",linkName)
        ));
       return client.listBy(FriendPost.class, friendPostListOptions, PageRequestImpl.ofSize(1))
            .flatMap(friendPosts -> {
                long total = friendPosts.getTotal();
                if (total > sum) {
                    int l = (int) (total - sum);
                    PageRequestImpl pageRequestImpl = PageRequestImpl.of(pageNullSafe(1), sizeNullSafe(l),
                        Sort.by(asc("spec.pubDate")));
                   return client.listBy(FriendPost.class, friendPostListOptions, pageRequestImpl)
                       .flatMap(result -> Flux.fromIterable(result.getItems()).flatMap(client::delete).then());
                }
                return Mono.just(false);
            }).then();
    }


    public List<FriendPost> fetchFriendPost(String rssAddress,int postsLimit) {
        List<FriendPost> friendPostList = new ArrayList<>();
        try {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(rssAddress)));
            int postCount = 0;
            for (SyndEntry entry : feed.getEntries()) {
                Date publishedDate = entry.getPublishedDate();
                String value = entry.getDescription().getValue();
                FriendPost friendPost = new FriendPost();
                friendPost.setSpec(new FriendPost.FriendPostSpec());
                friendPost.getSpec().setTitle(entry.getTitle());
                friendPost.getSpec().setPostLink(entry.getLink());
                friendPost.getSpec().setPubDate(publishedDate.toInstant());
                friendPost.getSpec().setDescription(value);
                friendPostList.add(friendPost);
                postCount++;
                if (postCount >= postsLimit) {
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return friendPostList;
    }

    static Sort defaultSort() {
        return Sort.by("metadata.creationTimestamp").descending();
    }

    static int pageNullSafe(Integer page) {
        return ObjectUtils.defaultIfNull(page, 1);
    }

    static int sizeNullSafe(Integer size) {
        return ObjectUtils.defaultIfNull(size, 10);
    }

    public Mono<Integer> getSuccessfulRetainLimit(){
        return client.fetch(CronFriendPost.class, "cron-friends-default")
            .flatMap(cronFriendPost -> {
                int successfulRetainLimit =
                    cronFriendPost.getSpec().getSuccessfulRetainLimit();
                if (successfulRetainLimit == 0) {
                    successfulRetainLimit = 5;
                }
                return Mono.just(successfulRetainLimit);
            }).defaultIfEmpty(5);
    }
}
