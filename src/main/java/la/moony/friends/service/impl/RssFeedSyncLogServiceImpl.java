package la.moony.friends.service.impl;

import la.moony.friends.ListedRssSyncLog;
import la.moony.friends.extension.Link;
import la.moony.friends.extension.RssFeedSyncLog;
import la.moony.friends.query.RssSyncLogQuery;
import la.moony.friends.service.RssFeedSyncLogService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListOptions;
import run.halo.app.extension.ListResult;
import run.halo.app.extension.PageRequestImpl;
import run.halo.app.extension.ReactiveExtensionClient;
import java.util.function.Function;

@Component
public class RssFeedSyncLogServiceImpl implements RssFeedSyncLogService {

    private final ReactiveExtensionClient client;

    public RssFeedSyncLogServiceImpl(ReactiveExtensionClient client) {
        this.client = client;
    }

    @Override
    public Mono<ListResult<ListedRssSyncLog>> listRssSyncLog(RssSyncLogQuery query) {
        return buildListOptions(query)
            .flatMap(listOptions -> client.listBy(Link.class, listOptions,
                PageRequestImpl.of(query.getPage(), query.getSize(), query.getSort())
            ))
            .flatMap(listResult -> Flux.fromStream(listResult.get())
                .map(this::getListedRssSyncLog)
                .flatMapSequential(Function.identity())
                .collectList()
                .map(listedApplications -> new ListResult<>(listResult.getPage(), listResult.getSize(),
                    listResult.getTotal(), listedApplications)
                )
                .defaultIfEmpty(ListResult.emptyResult())
            );
    }

    private Mono<ListedRssSyncLog> getListedRssSyncLog(Link link) {
        Assert.notNull(link, "The link must not be null.");
        var listedRssSyncLog = new ListedRssSyncLog().setLink(link);
        var logMono = client.fetch(RssFeedSyncLog.class,"sync-log-"+link.getMetadata().getName())
            .doOnNext(listedRssSyncLog::setLog);

        return Mono.when(logMono)
            .thenReturn(listedRssSyncLog);
    }

    Mono<ListOptions> buildListOptions(RssSyncLogQuery query) {
        return Mono.just(query.toListOptions());
    }
}
