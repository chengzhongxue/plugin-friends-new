package la.moony.friends.reconciler;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import la.moony.friends.extension.CronFriendPost;
import la.moony.friends.extension.FriendPost;
import la.moony.friends.extension.Link;
import la.moony.friends.extension.SyncFriendPost;
import la.moony.friends.util.CommonUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import run.halo.app.extension.ExtensionClient;
import run.halo.app.extension.ExtensionUtil;
import run.halo.app.extension.ListOptions;
import run.halo.app.extension.ListResult;
import run.halo.app.extension.Metadata;
import run.halo.app.extension.PageRequestImpl;
import run.halo.app.extension.controller.Controller;
import run.halo.app.extension.controller.ControllerBuilder;
import run.halo.app.extension.controller.Reconciler;
import java.net.URL;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import run.halo.app.extension.router.selector.FieldSelector;

import static org.springframework.data.domain.Sort.Order.asc;
import static run.halo.app.extension.ExtensionUtil.addFinalizers;
import static run.halo.app.extension.ExtensionUtil.removeFinalizers;
import static run.halo.app.extension.MetadataUtil.nullSafeAnnotations;
import static run.halo.app.extension.index.query.QueryFactory.equal;

@Component
public class SyncFriendPostReconciler implements Reconciler<Reconciler.Request>  {


    private static final Logger log = LoggerFactory.getLogger(SyncFriendPost.class);
    private static final String FINALIZER_NAME = "sync-friend-post-protection";
    private final ExtensionClient client;

    private Clock clock;

    public SyncFriendPostReconciler(ExtensionClient client) {
        this.client = client;
        this.clock = Clock.systemDefaultZone();
    }

    void setClock(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Result reconcile(Request request) {
        client.fetch(SyncFriendPost.class, request.name())
            .ifPresent(sync -> {
                var spec = sync.getSpec();
                var status = sync.getStatus();
                if (ExtensionUtil.isDeleted(sync)) {
                    if (removeFinalizers(sync.getMetadata(), Set.of(FINALIZER_NAME))) {
                        client.update(sync);
                    }
                    return;
                }
                if (addFinalizers(sync.getMetadata(), Set.of(FINALIZER_NAME))) {
                    Optional<Link> link = client.fetch(Link.class, spec.getLinkName());
                    if (link.isPresent()) {
                        this.tryToSynchronizeFriendPost(sync, (Link)link.get());
                    }else {
                        log.info("Synchronization {} stopped because link {} was not found or deleted", sync.getMetadata().getName(), spec.getLinkName());
                        status.setPhase(SyncFriendPost.Phase.FAILED);
                        status.setFailureReason("LinkNotFound");
                        status.setFailureMessage("The link was not found or deleted.");
                        client.update(sync);
                    }
                }
            });
        return Result.doNotRetry();
    }

    private void tryToSynchronizeFriendPost(SyncFriendPost sync, Link link) {
        String syncName = sync.getMetadata().getName();
        var linkName = link.getMetadata().getName();
        var annotations = nullSafeAnnotations(link);
        var rssUri = annotations.get("rss_uri");
        Optional<CronFriendPost> cron = client.fetch(CronFriendPost.class, "cron-friends-default");
        CronFriendPost cronFriendPost = cron.get();
        int sum = cronFriendPost.getSpec().getSuccessfulRetainLimit();
        if (sum == 0) {
            sum = 5;
        }
        var status = sync.getStatus();
        var spec = sync.getSpec();
        int SyncCount = 0;
        spec.setSyncCount(SyncCount);
        status.setStartTimestamp(clock.instant());
        List<FriendPost> friendPostList = fetchFriendPost(rssUri, sum,sync);
        if (friendPostList == null || friendPostList.size() == 0) {
            updateSync(syncName,sync);
        }else {
            for (FriendPost rssPost : friendPostList) {
                var friendPostListOptions = new ListOptions();
                friendPostListOptions.setFieldSelector(FieldSelector.of(
                    equal("spec.postLink",rssPost.getSpec().getPostLink())
                ));
                long total = client.listBy(FriendPost.class, friendPostListOptions,
                    PageRequestImpl.ofSize(1)).getTotal();
                if (total==0) {
                    FriendPost friendPost = new FriendPost();
                    // 设置元数据才能保存
                    friendPost.setMetadata(new Metadata());
                    friendPost.getMetadata().setGenerateName("friend-post-");
                    friendPost.setSpec(rssPost.getSpec());
                    var friendPostSpec = friendPost.getSpec();
                    friendPostSpec.setLogo(link.getSpec().getLogo());
                    friendPostSpec.setAuthor(link.getSpec().getDisplayName());
                    friendPostSpec.setAuthorUrl(link.getSpec().getUrl());
                    friendPostSpec.setLinkName(linkName);
                    String description = friendPost.getSpec().getDescription();
                    //解析html内容转换成文本
                    if (StringUtils.isNotEmpty(description)){
                        description = CommonUtils.parseAndTruncateHtml2Text(description, 200);
                        String regexp = "[　*|\\s*]*";
                        description = description.replaceFirst(regexp, "").trim();
                        friendPostSpec.setDescription(description);
                    }
                    SyncCount+=1;
                    client.create(friendPost);
                }
            }
            spec.setSyncCount(SyncCount);
            delFriendPost(linkName,sum);
            updateSync(syncName,sync);
        }

    }

    public List<FriendPost> fetchFriendPost(String rssAddress,int postsLimit, SyncFriendPost sync) {
        List<FriendPost> friendPostList = new ArrayList<>();
        var spec = sync.getSpec();
        var status = sync.getStatus();
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
            status.setPhase(SyncFriendPost.Phase.SUCCEEDED);
            status.setCompletionTimestamp(clock.instant());
        } catch (Exception e) {
            status.setPhase(SyncFriendPost.Phase.FAILED);
            status.setFailureReason("CrawlingRSSError");
            status.setFailureMessage(e.getMessage());
            log.debug("error in crawling rss", e);
            return null;
        }
        return friendPostList;
    }

    public void delFriendPost(String linkName,Integer sum) {
        var friendPostListOptions = new ListOptions();
        friendPostListOptions.setFieldSelector(FieldSelector.of(
            equal("spec.linkName",linkName)
        ));
        long total = client.listBy(FriendPost.class, friendPostListOptions, PageRequestImpl.ofSize(1)).getTotal();
        if (total > sum) {
            int l = (int) (total - sum);
            PageRequestImpl pageRequestImpl = PageRequestImpl.of(pageNullSafe(1), sizeNullSafe(l),
                Sort.by(asc("spec.pubDate")));
            ListResult<FriendPost> friendPosts =
                client.listBy(FriendPost.class, friendPostListOptions, pageRequestImpl);
            for (FriendPost friendPost : friendPosts) {
                client.delete(friendPost);
            }
        }
    }

    private void updateSync(String name, SyncFriendPost sync) {
        this.client.fetch(SyncFriendPost.class, name).ifPresent((syncFriendPost) -> client.update(sync));
    }

    static int pageNullSafe(Integer page) {
        return ObjectUtils.defaultIfNull(page, 1);
    }

    static int sizeNullSafe(Integer size) {
        return ObjectUtils.defaultIfNull(size, 10);
    }

    @Override
    public Controller setupWith(ControllerBuilder builder) {
        return builder.extension(new SyncFriendPost()).workerCount(3).build();
    }
}
