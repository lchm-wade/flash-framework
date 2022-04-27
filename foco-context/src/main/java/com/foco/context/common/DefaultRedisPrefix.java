package com.foco.context.common;
import com.foco.properties.CustomRedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/08/03 10:39
 */
public class DefaultRedisPrefix implements RedisPrefix {
    @Autowired
    private CustomRedisProperties redisProperties;
    @Override
    public String getPrefix() {
        return redisProperties.getRedisKeyPrefix();
    }
}
