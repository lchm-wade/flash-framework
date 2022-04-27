package com.foco.shadow.redis;
import com.foco.context.common.RedisPrefix;
import com.foco.context.util.HttpContext;
import com.foco.properties.CustomRedisProperties;
import com.foco.properties.SystemConfig;
import com.foco.shadow.properties.ShadowProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/08/03 10:39
 */
public class ShadowRedisPrefix implements RedisPrefix {
    @Autowired
    private CustomRedisProperties redisProperties;
    @Autowired
    private ShadowProperties shadowProperties;
    @Override
    public String getPrefix() {
        if("true".equals(HttpContext.getHeader(SystemConfig.getConfig().getShadowHead()))){
            //影子库压测
            return shadowProperties.getFlag()+":"+redisProperties.getRedisKeyPrefix();
        }
        return redisProperties.getRedisKeyPrefix();
    }
}
