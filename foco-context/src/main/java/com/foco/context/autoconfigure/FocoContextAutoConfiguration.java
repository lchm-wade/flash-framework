package com.foco.context.autoconfigure;

import com.foco.context.common.DefaultRedisPrefix;
import com.foco.context.common.RedisPrefix;
import com.foco.context.common.DefaultRequestHeaderTransmit;
import com.foco.context.core.FocoContextManager;
import com.foco.context.core.SpringContextHolder;
import com.foco.context.util.BootStrapPrinter;
import com.foco.model.constant.MainClassConstant;
import com.foco.properties.RequestHeaderTransmitProperties;
import com.foco.properties.SystemConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-06-29 14:48
 */
public class FocoContextAutoConfiguration {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-context",this.getClass());
    }
    @Bean
    @ConditionalOnMissingClass(MainClassConstant.FOCO_SHADOW)
    RedisPrefix defaultRedisPrefix(){
        return new DefaultRedisPrefix();
    }
    @Bean
    @ConditionalOnProperty(name = "foco.shadow.enabled",havingValue = "false")
    RedisPrefix defaultRedisPrefixAlias(){
        return new DefaultRedisPrefix();
    }
    @Bean
    DefaultRequestHeaderTransmit tokenRequestHeaderFilter(SystemConfig systemConfig, RequestHeaderTransmitProperties properties){
        return new DefaultRequestHeaderTransmit(systemConfig,properties);
    }
    @Bean
    FocoContextManager focoContextManager(){
        return new FocoContextManager();
    }
}
