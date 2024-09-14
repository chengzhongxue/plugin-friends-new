package la.moony.friends.vo;

import la.moony.friends.extension.LinkGroup;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import run.halo.app.extension.MetadataOperator;
import run.halo.app.theme.finders.vo.ExtensionVoOperator;

import java.util.List;


@Value
@Builder
public class LinkGroupVo implements ExtensionVoOperator {

    MetadataOperator metadata;

    LinkGroup.LinkGroupSpec spec;

    @With
    List<LinkVo> links;

    public static LinkGroupVo from(LinkGroup linkGroup) {
        return LinkGroupVo.builder()
            .metadata(linkGroup.getMetadata())
            .spec(linkGroup.getSpec())
            .links(List.of())
            .build();
    }
}
