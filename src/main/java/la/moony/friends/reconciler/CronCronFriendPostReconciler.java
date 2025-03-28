package la.moony.friends.reconciler;

import la.moony.friends.RssFeedSyncEvent;
import la.moony.friends.extension.CronFriendPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;
import run.halo.app.extension.ExtensionClient;
import run.halo.app.extension.ExtensionUtil;
import run.halo.app.extension.controller.Controller;
import run.halo.app.extension.controller.ControllerBuilder;
import run.halo.app.extension.controller.Reconciler;
import java.time.*;
import java.util.Objects;

@Component
public class CronCronFriendPostReconciler implements Reconciler<Reconciler.Request>{

    private static final Logger log = LoggerFactory.getLogger(CronCronFriendPostReconciler.class);

    private final ExtensionClient client;
    private Clock clock;
    private final ApplicationEventPublisher eventPublisher;


    public CronCronFriendPostReconciler(ExtensionClient client,
        ApplicationEventPublisher eventPublisher) {
        this.client = client;
        this.eventPublisher = eventPublisher;
        this.clock = Clock.systemDefaultZone();
    }


    void setClock(Clock clock) {
        this.clock = clock;
    }


    public Result reconcile(Request request) {
        return (Result)this.client.fetch(CronFriendPost.class, request.name()).map((cronFriendPost) -> {
            if (ExtensionUtil.isDeleted(cronFriendPost)) {
                return Result.doNotRetry();
            } else {
                CronFriendPost.CronSpec spec = cronFriendPost.getSpec();
                if (!spec.isSuspend()) {
                    return Result.doNotRetry();
                } else {
                    String cron = spec.getCron();
                    String timezone = spec.getTimezone();
                    ZoneId zoneId = ZoneId.systemDefault();
                    if (timezone != null) {
                        try {
                            zoneId = ApplicationConversionService.getSharedInstance().convert(timezone, ZoneId.class);
                        } catch (DateTimeException dateTimeException) {
                            log.error("Invalid zone ID {}", timezone, dateTimeException);
                            return Result.doNotRetry();
                        }
                    }

                    Instant now = Instant.now(this.clock);
                    if (!CronExpression.isValidExpression(cron)) {
                        log.error("Cron expression {} is invalid.", cron);
                        return Result.doNotRetry();
                    } else {
                        CronExpression cronExp = CronExpression.parse(cron);
                        CronFriendPost.CronStatus status = cronFriendPost.getStatus();
                        Instant lastScheduledTimestamp = status.getLastScheduledTimestamp();
                        if (lastScheduledTimestamp == null) {
                            lastScheduledTimestamp = cronFriendPost.getMetadata().getCreationTimestamp();
                        }

                        ZonedDateTime nextFromNow = cronExp.next(now.atZone(zoneId));
                        ZonedDateTime nextFromLast = cronExp.next(lastScheduledTimestamp.atZone(zoneId));

                        if (nextFromNow != null && nextFromLast != null) {
                            if (Objects.equals(nextFromNow, nextFromLast)) {
                                log.info("Skip scheduling and next scheduled at {}", nextFromNow);
                                status.setNextSchedulingTimestamp(nextFromNow.toInstant());
                                this.client.update(cronFriendPost);
                                return new Result(true, Duration.between(now, nextFromNow));
                            } else {


                                eventPublisher.publishEvent(new RssFeedSyncEvent(this, "cron-friends-default", "all"));

                                ZonedDateTime zonedNow = now.atZone(zoneId);
                                ZonedDateTime scheduleTimestamp = now.atZone(zoneId);

                                ZonedDateTime next;
                                for(next = lastScheduledTimestamp.atZone(zoneId); next != null && next.isBefore(zonedNow); next = cronExp.next(next)) {
                                    scheduleTimestamp = next;
                                }

                                status.setLastScheduledTimestamp(scheduleTimestamp.toInstant());
                                if (next != null) {
                                    status.setNextSchedulingTimestamp(next.toInstant());
                                }

                                this.client.update(cronFriendPost);
                                log.info("Scheduled at {} and next scheduled at {}", scheduleTimestamp, next);
                                return new Result(true, Duration.between(now, next));
                            }

                        } else {
                            return Result.doNotRetry();
                        }

                    }

                }
            }

         }).orElseGet(Result::doNotRetry);
    }


    public Controller setupWith(ControllerBuilder builder) {
        return builder.extension(new CronFriendPost()).workerCount(1).build();
    }
}
