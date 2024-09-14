package la.moony.friends.service.impl;

import la.moony.friends.service.SettingConfigFriends;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.ReactiveSettingFetcher;

@Component
@RequiredArgsConstructor
public class SettingConfigFriendsImpl implements SettingConfigFriends {

    private final ReactiveSettingFetcher settingFetcher;

    @Override
    public Mono<BaseConfig> getBaseConfig() {
        return settingFetcher.fetch(BaseConfig.GROUP, BaseConfig.class)
            .defaultIfEmpty(new BaseConfig());
    }

}
