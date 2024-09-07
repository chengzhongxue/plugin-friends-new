package la.moony.friends.rest;

import la.moony.friends.service.FriendPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.ApiVersion;


@ApiVersion("v1alpha1")
@RequestMapping("/friendPost")
@RestController
@Slf4j
public class FriendPostController {


    private final FriendPostService friendPostService;


    public FriendPostController(FriendPostService friendPostService) {
        this.friendPostService = friendPostService;
    }

    @PostMapping("/synchronizationFriend")
    public Mono<Void> synchronizationFriend() {
        return friendPostService.synchronizationFriend().then();
    }

}
