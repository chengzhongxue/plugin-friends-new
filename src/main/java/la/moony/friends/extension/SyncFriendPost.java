package la.moony.friends.extension;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@GVK(kind = "SyncFriendPost", group = "friend.moony.la",
    version = "v1alpha1", singular = "syncfriendpost", plural = "syncfriendposts")
public class SyncFriendPost extends AbstractExtension {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private SyncFriendPostSpec spec;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private SyncFriendPostStatus status;

    @Data
    public static class SyncFriendPostSpec {
        @Schema(required = true)
        private String linkName;

        @Schema(required = true)
        private String linkDisplayName;

        @Schema(required = true)
        private String rss;

        private Integer SyncCount;
    }

    @Data
    public static class SyncFriendPostStatus {
        private Instant startTimestamp;
        private Instant completionTimestamp;
        private Phase phase;
        private String failureReason;
        private String failureMessage;
    }

    public static enum Phase {
        PENDING,
        RUNNING,
        SUCCEEDED,
        FAILED;

        private Phase() {
        }
    }
}