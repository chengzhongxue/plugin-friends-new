package la.moony.friends.extension;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;


@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@GVK(
    group = "friend.moony.la",
    version = "v1alpha1",
    kind = "CronFriendPost",
    singular = "cronfriendpost",
    plural = "cronfriendposts"
)
public class CronFriendPost extends AbstractExtension {


    private CronSpec spec = new CronSpec();

    private CronStatus status = new CronStatus();

    @Data
    public static class CronSpec {
        private String cron;
        private String timezone;
        private boolean suspend;

        @Schema(
            minimum = "0"
        )
        private int successfulRetainLimit;

        private List<String> disableSyncList;

    }

    @Data
    public static class CronStatus {
        private Instant lastScheduledTimestamp;
        private Instant nextSchedulingTimestamp;

    }




}
