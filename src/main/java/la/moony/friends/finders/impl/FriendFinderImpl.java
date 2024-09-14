package la.moony.friends.finders.impl;

import jakarta.annotation.Nonnull;
import la.moony.friends.extension.FriendPost;
import la.moony.friends.extension.Link;
import la.moony.friends.extension.LinkGroup;
import la.moony.friends.finders.FriendFinder;
import la.moony.friends.vo.FriendPostVo;
import la.moony.friends.vo.LinkGroupVo;
import la.moony.friends.vo.LinkVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import run.halo.app.extension.*;
import run.halo.app.extension.router.selector.FieldSelector;
import run.halo.app.theme.finders.Finder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    public Mono<ListResult<FriendPostVo>> list(Integer page, Integer size) {
        var pageRequest = PageRequestImpl.of(pageNullSafe(page), sizeNullSafe(size), defaultSort());
        return pageFriendPost(null, pageRequest);
    }


    @Override
    public Mono<FriendPostVo> getByName(String friendPostName) {
        return client.fetch(FriendPost.class, friendPostName)
            .map(FriendPostVo::from);
    }

    @Override
    public Mono<ListResult<FriendPostVo>> listByLinkName(Integer page, Integer size,String linkName) {
        var query = equal("spec.linkName", linkName);
        var pageRequest = PageRequestImpl.of(pageNullSafe(page), sizeNullSafe(size), defaultSort());
        return pageFriendPost(FieldSelector.of(query), pageRequest);
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

    private Mono<ListResult<FriendPostVo>> pageFriendPost(FieldSelector fieldSelector, PageRequest page){
        var listOptions = new ListOptions();
        var query = all();
        if (fieldSelector != null && fieldSelector.query() != null) {
            query = and(query, fieldSelector.query());
        }
        listOptions.setFieldSelector(FieldSelector.of(query));
        return client.listBy(FriendPost.class, listOptions, page)
            .flatMap(list -> Flux.fromStream(list.get())
                .concatMap(this::getFriendPostVo)
                .collectList()
                .map(friendPostVos -> new ListResult<>(list.getPage(), list.getSize(),
                    list.getTotal(), friendPostVos)
                )
            )
            .defaultIfEmpty(
                new ListResult<>(page.getPageNumber(), page.getPageSize(), 0L, List.of()));

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
            .map(LinkVo::from)
            .concatMap(link -> friendPostListBy(link.getMetadata().getName())
                .map(link::withFriendPosts)
            )
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

    int pageNullSafe(Integer page) {
        return ObjectUtils.defaultIfNull(page, 1);
    }

    int sizeNullSafe(Integer size) {
        return ObjectUtils.defaultIfNull(size, 10);
    }


}
