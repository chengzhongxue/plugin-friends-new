package la.moony.friends;

import io.swagger.v3.oas.annotations.media.Schema;
import la.moony.friends.extension.Link;
import la.moony.friends.extension.RssFeedSyncLog;
import lombok.Data;
import lombok.experimental.Accessors;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Accessors(chain = true)
public class ListedRssSyncLog {

    @Schema(requiredMode = REQUIRED)
    private Link link;

    private RssFeedSyncLog log;
}
