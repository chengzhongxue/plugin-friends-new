package la.moony.friends.finders.impl;

import jakarta.annotation.Nonnull;
import la.moony.friends.extension.FriendPost;
import la.moony.friends.extension.Link;
import la.moony.friends.extension.LinkGroup;
import la.moony.friends.extension.RssFeedSyncLog;
import la.moony.friends.finders.FriendFinder;
import la.moony.friends.util.SortUtils;
import la.moony.friends.vo.FriendPostVo;
import la.moony.friends.vo.LinkGroupVo;
import la.moony.friends.vo.LinkVo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.lang.NonNull;
import run.halo.app.extension.*;
import run.halo.app.extension.router.selector.FieldSelector;
import run.halo.app.infra.utils.JsonUtils;
import run.halo.app.theme.finders.Finder;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Order.asc;
import static run.halo.app.extension.index.query.QueryFactory.*;


@Finder("friendFinder")
@RequiredArgsConstructor
public class FriendFinderImpl implements FriendFinder {

    static final String UNGROUPED_NAME = "ungrouped";

    private final ReactiveExtensionClient client;



    @Override
    public Flux<FriendPostVo> listAll() {
        var listOptions = new ListOptions();
        var query = all();
        listOptions.setFieldSelector(FieldSelector.of(query));
        return client.listAll(FriendPost.class, listOptions, defaultSort())
            .flatMap(this::getFriendPostVo);
    }

    @Override
    public Mono<ListResult<FriendPostVo>> list(Map<String, Object> params) {
        var query = Optional.ofNullable(params)
            .map(map -> JsonUtils.mapToObject(map, FriendPostQuery.class))
            .orElseGet(FriendPostQuery::new);
        return pageFriendPost(query.toListOptions(), query.toPageRequest());
    }


    @Override
    public Mono<FriendPostVo> getByName(String friendPostName) {
        return client.fetch(FriendPost.class, friendPostName)
            .map(FriendPostVo::from);
    }

    @Override
    public Flux<LinkGroupVo> linkGroupBy() {
        return client.listAll(LinkGroup.class, new ListOptions(), defaultGroupSort())
            .map(LinkGroupVo::from)
            .concatMap(group -> linkListBy(group.getMetadata().getName())
                .collectList()
                .map(group::withLinks)
                .defaultIfEmpty(group)
            )
            .mergeWith(Mono.defer(() -> linkListBy(UNGROUPED_NAME)
                .collectList()
                // do not return ungrouped group if no links
                .filter(links -> !links.isEmpty())
                .flatMap(links -> ungrouped()
                    .map(LinkGroupVo::from)
                    .map(group -> group.withLinks(links))
                )
            ));
    }

    private Mono<ListResult<FriendPostVo>> pageFriendPost(ListOptions queryOptions, PageRequest page){
        var listOptions = new ListOptions();
        var query = all();
        listOptions.setFieldSelector(FieldSelector.of(query));
        var fieldSelector = queryOptions.getFieldSelector();
        if (fieldSelector != null) {
            listOptions.setFieldSelector(listOptions.getFieldSelector()
                .andQuery(fieldSelector.query()));
        }

        return client.listBy(FriendPost.class, listOptions, page)
            .flatMap(list -> Flux.fromStream(list.get())
                .concatMap(this::getFriendPostVo)
                .collectList()
                .map(friendPostVos -> new ListResult<>(list.getPage(), list.getSize(),
                    list.getTotal(), friendPostVos)
                )
            )
            .defaultIfEmpty(ListResult.emptyResult());

    }

    public Flux<LinkVo> linkListBy(String groupName) {
        var listOptions = new ListOptions();
        var query = isNull("metadata.deletionTimestamp");
        if (UNGROUPED_NAME.equals(groupName)) {
            query = and(query, isNull("spec.groupName"));
        } else {
            query = and(query, equal("spec.groupName", groupName));
        }
        listOptions.setFieldSelector(FieldSelector.of(query));
        return client.listAll(Link.class, listOptions, defaultLinkSort())
            .flatMapSequential(this::convertToListedVo)
            .collectList()
            .map(links -> {
                // 排序逻辑
                return links.stream()
                    .sorted(Comparator.comparing(link -> {
                            List<FriendPostVo> friendPosts = link.getFriendPosts();
                            if (friendPosts != null && !friendPosts.isEmpty()) {
                                return friendPosts.get(0).getSpec().getPubDate();
                            }
                            return null;
                        }, Comparator.nullsLast(Comparator.reverseOrder())
                    )).collect(Collectors.toList());
            })
            .flatMapMany(Flux::fromIterable);
    }


    public Mono<LinkVo> convertToListedVo(@NonNull Link link) {
        Assert.notNull(link, "Link must not be null");
        LinkVo linkVo = LinkVo.from(link);
        linkVo.setFriendPosts(List.of());
        return Mono.just(linkVo)
            .flatMap(lp -> friendPostListBy(lp.getMetadata().getName())
                .doOnNext(lp::setFriendPosts)
                .thenReturn(lp)
            ).flatMap(p -> {
                String linkName = p.getMetadata().getName();
                return client.fetch(RssFeedSyncLog.class,"sync-log-" + linkName)
                    .doOnNext(p::setRssFeedSyncLog)
                    .thenReturn(p);
            })
            .defaultIfEmpty(linkVo);
    }

    public Mono<List<FriendPostVo>> friendPostListBy(String linkName) {
        var listOptions = new ListOptions();
        var query = isNull("metadata.deletionTimestamp");
        query = and(query, equal("spec.linkName", linkName));
        listOptions.setFieldSelector(FieldSelector.of(query));
        var pageRequest = PageRequestImpl.of(pageNullSafe(1), sizeNullSafe(2), defaultSort());
        return client.listBy(FriendPost.class, listOptions, pageRequest)
            .flatMap(friendPosts -> Flux.fromStream(friendPosts.get())
                .map(FriendPostVo::from).collectList());
    }

    Mono<LinkGroup> ungrouped() {
        LinkGroup linkGroup = new LinkGroup();
        linkGroup.setMetadata(new Metadata());
        linkGroup.getMetadata().setName(UNGROUPED_NAME);
        linkGroup.setSpec(new LinkGroup.LinkGroupSpec());
        linkGroup.getSpec().setDisplayName("");
        linkGroup.getSpec().setPriority(0);
        return Mono.just(linkGroup);
    }

    static Sort defaultGroupSort() {
        return Sort.by(asc("spec.priority"),
            asc("metadata.creationTimestamp"),
            asc("metadata.name")
        );
    }

    static Sort defaultLinkSort() {
        return Sort.by(asc("spec.priority"),
            asc("metadata.creationTimestamp"),
            asc("metadata.name")
        );
    }

    static Sort defaultSort() {
        return Sort.by("spec.pubDate").descending();
    }

    private Mono<FriendPostVo> getFriendPostVo(@Nonnull FriendPost friendPost) {
        FriendPostVo friendPostVo = FriendPostVo.from(friendPost);
        return Mono.just(friendPostVo);
    }

    static int pageNullSafe(Integer page) {
        return ObjectUtils.defaultIfNull(page, 1);
    }

    static int sizeNullSafe(Integer size) {
        return ObjectUtils.defaultIfNull(size, 10);
    }

    @Data
    public static class FriendPostQuery {
        private Integer page;
        private Integer size;
        private String linkName;
        private List<String> sort;

        public ListOptions toListOptions() {
            var builder = ListOptions.builder();
            if (StringUtils.isNotBlank(linkName)) {
                builder.andQuery(equal("spec.linkName", linkName));
            }
            return builder.build();
        }

        public PageRequest toPageRequest() {
            return PageRequestImpl.of(pageNullSafe(getPage()),
                sizeNullSafe(getSize()), SortUtils.resolve(sort).and(defaultSort()));
        }
    }


}
