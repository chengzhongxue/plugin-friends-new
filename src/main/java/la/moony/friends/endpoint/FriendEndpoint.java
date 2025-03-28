package la.moony.friends.endpoint;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import la.moony.friends.ListedRssSyncLog;
import la.moony.friends.RssFeedSyncEvent;
import la.moony.friends.extension.FriendPost;
import la.moony.friends.extension.RssFeedSyncLog;
import la.moony.friends.query.FriendPostQuery;
import la.moony.friends.query.RssSyncLogQuery;
import la.moony.friends.service.RssDetailService;
import la.moony.friends.service.RssFeedSyncLogService;
import la.moony.friends.vo.RssDetail;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.endpoint.CustomEndpoint;
import run.halo.app.extension.GroupVersion;
import run.halo.app.extension.ListOptions;
import run.halo.app.extension.ListResult;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.extension.router.selector.FieldSelector;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static run.halo.app.extension.index.query.QueryFactory.isNull;

@Component
public class FriendEndpoint implements CustomEndpoint {

    private final String tag = "api.friend.moony.la/v1alpha1/FriendPost";


    private final ReactiveExtensionClient client;

    private final RssDetailService rssDetailService;

    private final ApplicationEventPublisher eventPublisher;

    private final RssFeedSyncLogService rssFeedSyncLogService;


    public FriendEndpoint(ReactiveExtensionClient client, RssDetailService rssDetailService,
        ApplicationEventPublisher eventPublisher, RssFeedSyncLogService rssFeedSyncLogService) {
        this.client = client;
        this.rssDetailService = rssDetailService;
        this.eventPublisher = eventPublisher;
        this.rssFeedSyncLogService = rssFeedSyncLogService;
    }

    @Override
    public RouterFunction<ServerResponse> endpoint() {
        return SpringdocRouteBuilder.route()
            .GET("friendposts", this::listFriendPost, builder -> {
                builder.operationId("ListFriendPosts")
                    .description("List friendPost.")
                    .tag(tag)
                    .response(
                    responseBuilder()
                        .implementation(ListResult.generateGenericClass(FriendPost.class))
                    );
                FriendPostQuery.buildParameters(builder);
            })
            .DELETE("friendposts/-/delete", this::deleteAllFriendPost, builder -> {
                builder.operationId("DeleteAllFriendPost")
                    .description("Delete All FriendPost.")
                    .tag(tag)
                    .response(responseBuilder()
                        .implementation(Void.class));
            })
            .GET("rsssynclogs", this::listRssSyncLog, builder -> {
                builder.operationId("ListRssSyncLogs")
                    .description("List RssSyncLog.")
                    .tag(tag)
                    .response(
                        responseBuilder()
                            .implementation(ListResult.generateGenericClass(ListedRssSyncLog.class))
                    );
                RssSyncLogQuery.buildParameters(builder);
            })
            .GET("parsingrss", this::parsingRss, builder -> {
                builder.operationId("ParsingRss")
                    .tag(tag)
                    .parameter(parameterBuilder()
                        .name("rssUrl")
                        .in(ParameterIn.QUERY)
                        .required(true)
                        .implementation(String.class)
                    )
                    .parameter(parameterBuilder()
                        .name("fetchLimitNumber")
                        .in(ParameterIn.QUERY)
                        .required(false)
                        .implementation(String.class)
                    )
                    .response(
                        responseBuilder()
                            .implementation(RssDetail.class)
                    );
            })
            .POST("syncrssfeed/{name}", this::syncRssFeed,
                builder -> builder.operationId("SyncRssFeed")
                    .tag(tag)
                    .parameter(parameterBuilder().name("name")
                        .in(ParameterIn.PATH)
                        .required(true)
                        .implementation(String.class))
                    .response(responseBuilder()
                        .implementation(Void.class))
            )
            .build();
    }


    Mono<ServerResponse> listFriendPost(ServerRequest request) {
        FriendPostQuery friendPostQuery = new FriendPostQuery(request);
        return client.listBy(FriendPost.class, friendPostQuery.toListOptions(),friendPostQuery.toPageRequest())
            .flatMap(friendPosts -> ServerResponse.ok().bodyValue(friendPosts));
    }

    Mono<ServerResponse> deleteAllFriendPost(ServerRequest request) {
        return client.listAll(FriendPost.class,new ListOptions().setFieldSelector(FieldSelector.of(isNull("metadata.deletionTimestamp"))), Sort.unsorted())
            .flatMap(friendPost -> client.delete(friendPost))
            .flatMap(friendPost -> client.listAll(RssFeedSyncLog.class,new ListOptions().setFieldSelector(FieldSelector.of(isNull("metadata.deletionTimestamp"))), Sort.unsorted())
                .flatMap(rssFeedSyncLog -> client.delete(rssFeedSyncLog))
            )
            .then(ServerResponse.ok().build());
    }

    Mono<ServerResponse> listRssSyncLog(ServerRequest request) {
        RssSyncLogQuery rssSyncLogQuery = new RssSyncLogQuery(request);
        return rssFeedSyncLogService.listRssSyncLog(rssSyncLogQuery)
            .flatMap(listRssSyncLogs -> ServerResponse.ok().bodyValue(listRssSyncLogs));
    }

    Mono<ServerResponse> parsingRss(ServerRequest request) {
        final var rssUrl = request.queryParam("rssUrl").orElseThrow();
        final var fetchLimitNumber = request.queryParam("fetchLimitNumber")
            .map(Integer::parseInt)
            .orElse(5);
        return rssDetailService.fetchRssDetail(rssUrl,fetchLimitNumber)
            .flatMap(rssDetail -> ServerResponse.ok().bodyValue(rssDetail));
    }

    private Mono<ServerResponse> syncRssFeed(ServerRequest request) {
        var name = request.pathVariable("name");
        return Mono.defer(() -> {
            eventPublisher.publishEvent(new RssFeedSyncEvent(this, "cron-friends-default", name));
            return ServerResponse.ok().build();
        });
    }

    @Override
    public GroupVersion groupVersion() {
        return GroupVersion.parseAPIVersion("api.friend.moony.la/v1alpha1");
    }
}
