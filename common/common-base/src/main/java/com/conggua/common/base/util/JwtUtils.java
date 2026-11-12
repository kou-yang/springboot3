package com.conggua.common.base.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @author ky
 * @description
 * @date 2024-05-15 09:45
 */
@Slf4j
@Component
public class JwtUtils {

    private static String ACCESS_SECRET;

    private static String REFRESH_SECRET;

    @Value("${jwt.access-token-secret}")
    public void setAccessSecret(String accessSecret) {
        JwtUtils.ACCESS_SECRET = accessSecret;
    }

    @Value("${jwt.refresh-token-secret}")
    public void setRefreshSecret(String refreshSecret) {
        JwtUtils.REFRESH_SECRET = refreshSecret;
    }

    private static final String USER_ID_CLAIM = "userId";
    private static final String TENANT_ID_CLAIM = "tenantId";
    private static final String MAP_CLAIM = "map";

    /**
     * 生成token（过期时间通过redis维护）
     * @param tenantId
     * @param userId
     * @param isAccess
     * @return
     */
    public static String generateToken(String tenantId, String userId, Boolean isAccess) {
        Algorithm algorithm = Algorithm.HMAC256(isAccess ? ACCESS_SECRET : REFRESH_SECRET);
        JWTCreator.Builder builder = JWT.create()
                .withClaim(USER_ID_CLAIM, userId)
                .withClaim(TENANT_ID_CLAIM, tenantId)
                .withIssuedAt(new Date());
        return builder.sign(algorithm);
    }

    /**
     * 生成token（过期时间通过redis维护）
     * @param map
     * @param isAccess
     * @return
     */
    public static String generateToken(Map<String, ?> map, Boolean isAccess) {
        Algorithm algorithm = Algorithm.HMAC256(isAccess ? ACCESS_SECRET : REFRESH_SECRET);
        JWTCreator.Builder builder = JWT.create()
                .withClaim(MAP_CLAIM, map)
                .withIssuedAt(new Date());
        return builder.sign(algorithm);
    }

    /**
     * 解析token
     * @param token
     * @return 校验通过返回 DecodedJWT，失败（缺失/格式错误/签名不符）返回 null，由调用方决定错误语义
     */
    public static DecodedJWT verify(String token, Boolean isAccess) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(isAccess ? ACCESS_SECRET : REFRESH_SECRET);
            return JWT.require(algorithm).build().verify(token);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取userId
     * @param jwt
     * @return
     */
    public static String getUserId(DecodedJWT jwt) {
        return Optional.ofNullable(jwt.getClaim(USER_ID_CLAIM)).map(Claim::asString).orElse(null);
    }

    /**
     * 获取tenantId
     * @param jwt
     * @return
     */
    public static String getTenantId(DecodedJWT jwt) {
        return Optional.ofNullable(jwt.getClaim(TENANT_ID_CLAIM)).map(Claim::asString).orElse(null);
    }

    /**
     * 获取map
     * @param jwt
     * @return
     */
    public static Map<String, ?> getMap(DecodedJWT jwt) {
        return Optional.ofNullable(jwt.getClaim(MAP_CLAIM)).map(Claim::asMap).orElse(Collections.emptyMap());
    }
}
