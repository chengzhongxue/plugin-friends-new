package la.moony.friends.service;

import java.util.List;
import lombok.Data;
import reactor.core.publisher.Mono;

public interface SettingConfigFriends {

    Mono<BaseConfig> getBaseConfig();


    @Data
    class BaseConfig {
        public static final String GROUP = "base";
        private List<String> disableSynchronizationList;
    }
}
