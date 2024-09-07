package la.moony.friends.finders.impl;

import jakarta.annotation.Nonnull;
import la.moony.friends.extension.FriendPost;
import la.moony.friends.finders.FriendFinder;
import la.moony.friends.vo.FriendPostVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import run.halo.app.extension.*;
import run.halo.app.extension.router.selector.FieldSelector;
import run.halo.app.theme.finders.Finder;
import java.util.List;

import static run.halo.app.extension.index.query.QueryFactory.*;


@Finder("friendFinder")
@RequiredArgsConstructor
public class FriendFinderImpl implements FriendFinder {

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
