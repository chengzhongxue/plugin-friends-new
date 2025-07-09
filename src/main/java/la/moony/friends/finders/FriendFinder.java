package la.moony.friends.finders;

import la.moony.friends.finders.impl.FriendFinderImpl;
import la.moony.friends.vo.FriendPostVo;
import la.moony.friends.vo.LinkGroupVo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;
import java.util.Map;
import org.springframework.lang.Nullable;

public interface FriendFinder {

    Flux<FriendPostVo> listAll();

    Mono<ListResult<FriendPostVo>> list(@Nullable Integer page, @Nullable Integer size);

    /**
     * Lists friendPosts by query params.
     *
     * @param params query params see {@link FriendFinderImpl.FriendPostQuery}
     */
    Mono<ListResult<FriendPostVo>> list(Map<String, Object> params);

    Mono<FriendPostVo> getByName(String friendPostName);

    Flux<LinkGroupVo> linkGroupBy();
}
