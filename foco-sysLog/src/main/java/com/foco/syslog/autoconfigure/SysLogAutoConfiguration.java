package com.foco.syslog.autoconfigure;

import com.foco.context.util.BootStrapPrinter;
import com.foco.syslog.properties.SysLogProperties;
import com.foco.model.constant.FocoConstants;
import com.foco.syslog.core.LogStoreProvider;
import com.foco.syslog.core.SysLogAspect;
import com.foco.syslog.core.SysLogThread;
import com.foco.syslog.provider.db.DbLogStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/22 19:12
 **/
@Configuration
@ConditionalOnProperty(prefix = SysLogProperties.PREFIX,name = FocoConstants.ENABLED)
@EnableConfigurationProperties(SysLogProperties.class)
public class SysLogAutoConfiguration {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-sysLog",this.getClass());
    }
    @Bean
    @ConditionalOnProperty(prefix = SysLogProperties.PREFIX,name = "store",havingValue = "db",matchIfMissing = true)
    LogStoreProvider dbLogStore(){
        return new DbLogStore();
    }
    @Bean
    SysLogAspect sysLogAspect(){
        return new SysLogAspect();
    }
    @Bean
    SysLogThread sysLogThread(){
        return new SysLogThread();
    }
}
