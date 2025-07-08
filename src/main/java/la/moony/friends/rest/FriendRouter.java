package la.moony.friends.rest;

import la.moony.friends.finders.FriendFinder;
import la.moony.friends.vo.FriendPostVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.ReactiveSettingFetcher;
import run.halo.app.theme.TemplateNameResolver;
import run.halo.app.theme.router.ModelConst;
import run.halo.app.theme.router.PageUrlUtils;
import run.halo.app.theme.router.UrlContextListResult;
import org.apache.commons.lang3.StringUtils;

import static run.halo.app.theme.router.PageUrlUtils.totalPage;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FriendRouter {

    
        private final FriendFinder friendFinder;
    
        private final TemplateNameResolver templateNameResolver;
    
        private final ReactiveSettingFetcher settingFetcher;
    
        @Bean
        RouterFunction<ServerResponse> friendTemplateRoute() {
            return RouterFunctions.route().GET("/friends",this::handlerFunction)
                .GET("/friends/page/{page:\\d+}",this::handlerFunction)
                .build();
        }
    
    
        private Mono<ServerResponse> handlerFunction(ServerRequest request) {
            return  templateNameResolver.resolveTemplateNameOrDefault(request.exchange(), "friends")
                .flatMap( templateName -> ServerResponse.ok().render(templateName,
                    java.util.Map.of(ModelConst.TEMPLATE_ID, templateName,
                        "friends", friendPostList(request),
                        "title",getFriendTitle())
                ));
        }

        Mono<String> getFriendTitle() {
            return this.settingFetcher.get("base").map(
                setting -> setting.get("title").asText("友链朋友圈")).defaultIfEmpty(
                "友链朋友圈");
        }
    
        private Mono<UrlContextListResult<FriendPostVo>> friendPostList(ServerRequest request) {
            String path = request.path();
            int pageNum = pageNumInPathVariable(request);
            String linkName = request.queryParam("linkName")
                .filter(StringUtils::isNotBlank)
                .orElse(null);
            return this.settingFetcher.get("base")
                .map(item -> item.get("pageSize").asInt(10))
                .defaultIfEmpty(10)
                .flatMap(pageSize -> {
                    if(StringUtils.isNotBlank(linkName)){
                        return friendFinder.listByLinkName(pageNum, pageSize, linkName)
                            .map(list -> new UrlContextListResult.Builder<FriendPostVo>()
                                .listResult(list)
                                .nextUrl(PageUrlUtils.nextPageUrl(path, totalPage(list)))
                                .prevUrl(PageUrlUtils.prevPageUrl(path))
                                .build()
                            );
                    }else{
                        return friendFinder.list(pageNum, pageSize)
                            .map(list -> new UrlContextListResult.Builder<FriendPostVo>()
                                .listResult(list)
                                .nextUrl(PageUrlUtils.nextPageUrl(path, totalPage(list)))
                                .prevUrl(PageUrlUtils.prevPageUrl(path))
                                .build()
                            );
                    }
                });
        }

    
        private int pageNumInPathVariable(ServerRequest request) {
            String page = request.pathVariables().get("page");
            return NumberUtils.toInt(page, 1);
        }

}
