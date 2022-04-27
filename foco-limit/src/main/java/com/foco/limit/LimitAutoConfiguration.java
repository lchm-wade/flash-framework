package com.foco.limit;

import com.foco.context.util.BootStrapPrinter;
import com.foco.limit.provider.LocalLimitRate;
import com.foco.limit.provider.ScheduledLimitCleaner;
import com.foco.model.constant.FocoConstants;
import com.foco.model.constant.MainClassConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/27 11:06
 **/
@Configuration
@ConditionalOnProperty(prefix = LimitProperties.PREFIX,name = FocoConstants.ENABLED)
@Import(RedisScriptConfiguration.class)
@EnableConfigurationProperties(LimitProperties.class)
public class LimitAutoConfiguration {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-limit",this.getClass());
    }
    @Bean
    public LimitRateAspect limitRateAspect(){
        return new LimitRateAspect();
    }
    @Bean
    @ConditionalOnMissingClass(MainClassConstant.SPRING_DATA_REDIS)
    public LimitRateSupport limitRateSupport(){
        return new LocalLimitRate();
    }
    @Bean
    @ConditionalOnMissingClass(MainClassConstant.SPRING_DATA_REDIS)
    public ScheduledLimitCleaner scheduledLimitClean(){
        return new ScheduledLimitCleaner();
    }
}
