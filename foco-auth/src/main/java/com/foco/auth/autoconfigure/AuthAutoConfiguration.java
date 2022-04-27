package com.foco.auth.autoconfigure;

import com.foco.auth.common.AuthLoginContextUpdater;
import com.foco.auth.common.GenericLoginAuth;
import com.foco.auth.common.GenericTokenManager;
import com.foco.auth.condition.ConditionalOnAuth;
import com.foco.auth.properties.TokenProperties;
import com.foco.auth.provider.db.DbTokenManager;
import com.foco.auth.provider.jwt.JwtLoginAuth;
import com.foco.auth.provider.jwt.JwtTokenManager;
import com.foco.auth.provider.redis.RedisTokenManager;
import com.foco.context.annotation.ConditionalOnAnyMatch;
import com.foco.context.core.LoginAuth;
import com.foco.model.constant.MainClassConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author ChenMing
 * @date 2021/10/9
 */
@EnableConfigurationProperties(TokenProperties.class)
public class AuthAutoConfiguration {
    @Bean
    @ConditionalOnAuth("db")
    public GenericTokenManager dbTokenManager() {
        return new DbTokenManager();
    }

    @Bean
    @ConditionalOnAuth("redis")
    public GenericTokenManager redisTokenManager() {
        return new RedisTokenManager();
    }

    @Bean
    @ConditionalOnMissingBean(GenericTokenManager.class)
    public GenericTokenManager jwtTokenManager() {
        return new JwtTokenManager();
    }

    @Bean
    @ConditionalOnAnyMatch(name = {"foco.token.token-type","foco.token.token-type"},value = {"redis","db"})
    public LoginAuth genericLoginAuth() {
        return new GenericLoginAuth();
    }

    @Bean
    @ConditionalOnMissingBean(LoginAuth.class)
    public LoginAuth jwtLoginAuth() {
        return new JwtLoginAuth();
    }
    @Bean
    public AuthLoginContextUpdater authLoginContextUpdater(){
        return new AuthLoginContextUpdater();
    }
}
