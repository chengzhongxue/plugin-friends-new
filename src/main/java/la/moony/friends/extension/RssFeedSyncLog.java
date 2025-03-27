package la.moony.friends.extension;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;
import java.time.Instant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@EqualsAndHashCode(callSuper = true)
@GVK(kind = "RssFeedSyncLog", group = "friend.moony.la",
    version = "v1alpha1", singular = "rssfeedsynclog", plural = "rssfeedsynclogs")
public class RssFeedSyncLog extends AbstractExtension {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String linkName;

    @Schema(requiredMode = REQUIRED,defaultValue = "nolink")
    private RssFeedSyncLogState state;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant syncTime;

    private String failureReason;

    private String failureMessage;

    public enum RssFeedSyncLogState {
        success,
        failed,
        onwait,
        nolink;

        private RssFeedSyncLogState() {
        }

    }
}