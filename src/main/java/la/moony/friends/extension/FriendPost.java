package la.moony.friends.extension;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

import java.time.Instant;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@GVK(kind = "FriendPost", group = "friend.moony.la",
    version = "v1alpha1", singular = "friendpost", plural = "friendposts")
public class FriendPost extends AbstractExtension {


    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private FriendPostSpec spec;

    @Data
    public static class FriendPostSpec {

        private String author;

        private String authorUrl;

        private String logo;

        private String title;

        private String postLink;

        private String description;

        private Instant pubDate;

        private String linkName;
    }

}
