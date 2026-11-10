package com.conggua.springboot3.server.designpattern.strategy.login;

import com.conggua.springboot3.server.constant.LoginMethodEnum;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author ky
 * @description
 * @date 2024-08-23 14:48
 */
@Configuration
public class LoginStrategyContext {

    private final Map<String, LoginStrategy> strategyMap;

    public LoginStrategyContext(Map<String, LoginStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public LoginStrategy getStrategy(Integer type) {
        return strategyMap.get(LoginMethodEnum.getValueByType(type));
    }
}
