package com.foco.limit;

import com.foco.limit.provider.RedisLimitRate;
import com.foco.model.constant.MainClassConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/27 11:06
 **/
@Configuration
@ConditionalOnClass(name= MainClassConstant.SPRING_DATA_REDIS)
public class RedisScriptConfiguration {
    @Bean
    public LimitRateSupport limitRateSupport(){
        return new RedisLimitRate();
    }
    @Bean(name = "redisLimit")
    @ConditionalOnMissingBean(name = "redisLimit")
    public DefaultRedisScript limitRedisScript() {
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript<Long>();
        defaultRedisScript.setResultType(Long.class);
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redisLimiter.lua")));
        return defaultRedisScript;
    }
}
