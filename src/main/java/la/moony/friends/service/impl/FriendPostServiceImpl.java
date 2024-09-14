package la.moony.friends.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import la.moony.friends.extension.Link;
import la.moony.friends.extension.SyncFriendPost;
import la.moony.friends.service.FriendPostService;
import la.moony.friends.service.SettingConfigFriends;
import la.moony.friends.util.LinkRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListOptions;
import run.halo.app.extension.Metadata;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.extension.Unstructured;
import run.halo.app.extension.router.selector.FieldSelector;

import java.util.List;
import java.util.Map;

import static run.halo.app.extension.MetadataUtil.nullSafeAnnotations;
import static run.halo.app.extension.index.query.QueryFactory.all;
import static run.halo.app.extension.index.query.QueryFactory.equal;

@Component
public class FriendPostServiceImpl implements FriendPostService {

    private static final Logger log = LoggerFactory.getLogger(FriendPostServiceImpl.class);

    private final ReactiveExtensionClient client;

    private final SettingConfigFriends settingConfigFriends;

    private final ObjectMapper objectMapper = Unstructured.OBJECT_MAPPER;

    public FriendPostServiceImpl(ReactiveExtensionClient client, SettingConfigFriends settingConfigFriends) {
        this.client = client;
        this.settingConfigFriends = settingConfigFriends;
    }

    @Override
    public Mono<Void> synchronizationFriend() {
        var listOptions = new ListOptions();
        listOptions.setFieldSelector(FieldSelector.of(all()));
        return client.listAll(Link.class, listOptions, defaultSort())
            .flatMap(link -> {
                var linkName = link.getMetadata().getName();
                return getRss(link).flatMap(rssUri -> settingConfigFriends.getBaseConfig()
                    .flatMap(baseConfig -> {
                    List<String> disableSynchronizationList =
                        baseConfig.getDisableSynchronizationList();
                    boolean isContains = false;
                    if (disableSynchronizationList!=null) {
                        isContains = disableSynchronizationList.contains(linkName);
                    }
                    if (StringUtils.isNotEmpty(rssUri) && !isContains) {
                        SyncFriendPost sync = new SyncFriendPost();
                        var spec = new SyncFriendPost.SyncFriendPostSpec();
                        var status = new SyncFriendPost.SyncFriendPostStatus();
                        status.setPhase(SyncFriendPost.Phase.PENDING);
                        Metadata metadata = new Metadata();
                        metadata.setGenerateName("friend-post-sync-");
                        spec.setLinkName(linkName);
                        spec.setRss(rssUri);
                        spec.setLinkDisplayName(link.getSpec().getDisplayName());
                        sync.setMetadata(metadata);
                        sync.setSpec(spec);
                        sync.setStatus(status);
                        var friendPostListOptions = new ListOptions();
                        friendPostListOptions.setFieldSelector(FieldSelector.of(
                            equal("spec.linkName",linkName)
                        ));
                        return client.listAll(SyncFriendPost.class, friendPostListOptions, defaultSort())
                            .flatMap(client::delete)
                            .then(client.create(sync).thenReturn(true));
                    }
                    return Mono.just(false);
                }));
            }).then();
    }


    public Mono<String> getRss(Link link) {
        var annotations = nullSafeAnnotations(link);
        String rssUri = annotations.getOrDefault("rss_uri","");
        String isRequest = annotations.getOrDefault("is_request","");
        if (StringUtils.isEmpty(rssUri) && StringUtils.isEmpty(isRequest)) {
            String linkRss = LinkRequest.getLinkRss(link.getSpec().getUrl());
            annotations.put("is_request","true");
            if (StringUtils.isNotEmpty(linkRss)) {
                annotations.put("rss_uri",linkRss);
            }
            return updateLink(link).then(Mono.just(linkRss));
        }
        return Mono.just(rssUri);
    }


    public Mono<Link> updateLink(Link link) {
        Map extensionMap = objectMapper.convertValue(link, Map.class);
        var extension = new Unstructured(extensionMap);
        return client.update(extension).flatMap(unstructured -> {
            var linkNew = objectMapper.convertValue(unstructured, Link.class);
            return Mono.just(linkNew);
        });
    }


    static Sort defaultSort() {
        return Sort.by("metadata.creationTimestamp").descending();
    }
}
