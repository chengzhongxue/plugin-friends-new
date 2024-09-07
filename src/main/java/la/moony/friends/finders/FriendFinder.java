package la.moony.friends.finders;

import la.moony.friends.vo.FriendPostVo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;

public interface FriendFinder {

    Flux<FriendPostVo> listAll();

    Mono<ListResult<FriendPostVo>> list(Integer page, Integer size);

    Mono<FriendPostVo> getByName(String friendPostName);

    Mono<ListResult<FriendPostVo>> listByLinkName(Integer page, Integer size,String linkName);
}
