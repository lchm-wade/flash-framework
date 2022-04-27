package com.foco.shadow.autoconfigure;

import com.foco.context.common.RedisPrefix;
import com.foco.context.util.BootStrapPrinter;
import com.foco.model.constant.DbInterceptorOrderConstants;
import com.foco.model.constant.FocoConstants;
import com.foco.model.constant.MainClassConstant;
import com.foco.shadow.redis.ShadowRedisPrefix;
import com.foco.shadow.db.DefaultShadowTableNameHandler;
import com.foco.shadow.db.ShadowTableInterceptor;
import com.foco.shadow.db.ShadowTableSqlParser;
import com.foco.shadow.properties.ShadowProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/09 17:06
 */
@Configuration
@EnableConfigurationProperties(ShadowProperties.class)
@ConditionalOnProperty(prefix = ShadowProperties.PREFIX, name = FocoConstants.ENABLED, matchIfMissing = true)
@Slf4j
public class ShadowAutoConfiguration{
    @Bean
    RedisPrefix shadowRedisPrefix(){
        return new ShadowRedisPrefix();
    }
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-shadow",this.getClass());
    }


    @Order(DbInterceptorOrderConstants.shadowTableInterceptor)
    @Bean
    @ConditionalOnClass(name=MainClassConstant.MYBATIS_PLUS)
    public ShadowTableInterceptor dbTableInterceptor(ShadowProperties properties) {
        log.info("enabled dbTableShadow");
        ShadowTableSqlParser sqlParser = new ShadowTableSqlParser(new DefaultShadowTableNameHandler(properties));
        return new ShadowTableInterceptor(sqlParser);
    }
}
