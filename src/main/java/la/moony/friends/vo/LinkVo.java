package la.moony.friends.vo;

import la.moony.friends.extension.Link;
import la.moony.friends.extension.RssFeedSyncLog;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.With;
import lombok.experimental.SuperBuilder;
import run.halo.app.extension.MetadataOperator;
import run.halo.app.theme.finders.vo.ExtensionVoOperator;
import java.util.List;

@Data
@SuperBuilder
@ToString
@EqualsAndHashCode
public class LinkVo implements ExtensionVoOperator {

    MetadataOperator metadata;

    Link.LinkSpec spec;


    RssFeedSyncLog rssFeedSyncLog;

    List<FriendPostVo> friendPosts;

    public static LinkVo from(Link link) {
        return LinkVo.builder()
            .metadata(link.getMetadata())
            .spec(link.getSpec())
            .friendPosts(List.of())
            .build();
    }
}
