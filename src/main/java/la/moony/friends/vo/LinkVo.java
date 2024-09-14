package la.moony.friends.vo;

import la.moony.friends.extension.Link;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import run.halo.app.extension.MetadataOperator;
import run.halo.app.theme.finders.vo.ExtensionVoOperator;
import java.util.List;

@Value
@Builder
public class LinkVo implements ExtensionVoOperator {

    MetadataOperator metadata;

    Link.LinkSpec spec;

    @With
    List<FriendPostVo> friendPosts;

    public static LinkVo from(Link link) {
        return LinkVo.builder()
            .metadata(link.getMetadata())
            .spec(link.getSpec())
            .friendPosts(List.of())
            .build();
    }
}
