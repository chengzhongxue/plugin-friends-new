package la.moony.friends.service;

import la.moony.friends.ListedRssSyncLog;
import la.moony.friends.query.RssSyncLogQuery;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;

public interface RssFeedSyncLogService {

    Mono<ListResult<ListedRssSyncLog>> listRssSyncLog(RssSyncLogQuery query);

    Mono<Void> publishRssFeedSyncEvent(String name);
}
