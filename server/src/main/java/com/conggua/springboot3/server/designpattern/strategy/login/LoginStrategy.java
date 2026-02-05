package com.conggua.springboot3.server.designpattern.strategy.login;

import com.conggua.common.base.util.JwtUtils;
import com.conggua.common.redis.util.RedisUtils;
import com.conggua.springboot3.server.constant.LoginConstant;
import com.conggua.springboot3.server.constant.RedisKey;
import com.conggua.springboot3.server.model.bo.UserInfo;
import com.conggua.springboot3.server.model.dto.UserLoginDTO;
import com.conggua.springboot3.server.model.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ky
 * @description
 * @date 2024-08-23 14:43
 */
public interface LoginStrategy {

    /**
     * 执行逻辑
     * @param dto
     * @return
     */
    Map<String, String> execute(UserLoginDTO dto);

    /**
     * 生成token
     * @param user
     * @return
     */
    default Map<String, String> generateToken(User user) {
        String userId = user.getId();
        String accessToken = RedisUtils.get(RedisKey.getKeyNoTenant(RedisKey.ACCESS_TOKEN, userId), String.class);
        String refreshToken = RedisUtils.get(RedisKey.getKeyNoTenant(RedisKey.REFRESH_TOKEN, userId), String.class);
        if (StringUtils.isAnyBlank(accessToken, refreshToken)) {
            // 生成新的 AccessToken 和 RefreshToken
            Map<String, String> claimMap = Map.of("userId", userId);
            accessToken = JwtUtils.generateToken(claimMap, true);
            refreshToken = JwtUtils.generateToken(claimMap, false);
            // 存储到 redis
            RedisUtils.set(RedisKey.getKeyNoTenant(RedisKey.ACCESS_TOKEN, userId), accessToken, LoginConstant.ACCESS_TOKEN_EXPIRE, TimeUnit.MINUTES);
            RedisUtils.set(RedisKey.getKeyNoTenant(RedisKey.REFRESH_TOKEN, userId), refreshToken, LoginConstant.REFRESH_TOKEN_EXPIRE, TimeUnit.DAYS);

            // 保存用户信息到 redis
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(user, userInfo);
            RedisUtils.set(RedisKey.getKeyNoTenant(RedisKey.USER_INFO, userId), userInfo);
        }
        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }
}
