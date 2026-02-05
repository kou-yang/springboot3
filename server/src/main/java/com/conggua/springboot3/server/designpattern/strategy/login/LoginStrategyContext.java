package com.conggua.springboot3.server.designpattern.strategy.login;

import com.conggua.springboot3.server.constant.LoginMethodEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ky
 * @description
 * @date 2024-08-23 14:48
 */
@Configuration
public class LoginStrategyContext {

    @Autowired
    private final Map<String, LoginStrategy> strategyMap = new HashMap<>();

    public LoginStrategy getStrategy(Integer type) {
        return strategyMap.get(LoginMethodEnum.getValueByType(type));
    }
}
