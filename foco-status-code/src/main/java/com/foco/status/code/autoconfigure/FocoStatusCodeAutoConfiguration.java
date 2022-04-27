package com.foco.status.code.autoconfigure;

import com.foco.context.core.SpringContextHolder;
import com.foco.context.util.BootStrapPrinter;
import com.foco.model.constant.MainClassConstant;
import com.foco.status.code.handler.DefaultStatusCodeHandler;
import com.foco.status.code.handler.IStatusCodeHandler;
import com.foco.status.code.handler.InternationalStatusCodeHandler;
import com.foco.status.code.serialize.ResponseBodyStatusCodeConverterSerialize;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-06-29 14:48
 */
@Configuration
public class FocoStatusCodeAutoConfiguration {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-status-code",this.getClass());
    }
    @Bean
    @ConditionalOnClass(name = MainClassConstant.FOCO_INTERNATIONAL)
    IStatusCodeHandler internationalStatusCodeHandler(SpringContextHolder springContextHolder){
        return new InternationalStatusCodeHandler(springContextHolder);
    }
    @Bean
    @ConditionalOnMissingClass(MainClassConstant.FOCO_INTERNATIONAL)
    IStatusCodeHandler statusCodeHandler(){
        return new DefaultStatusCodeHandler();
    }
    @Bean
    ResponseBodyStatusCodeConverterSerialize responseBodyStatusCodeConverterSerialize(IStatusCodeHandler statusCodeHandler){
        return new ResponseBodyStatusCodeConverterSerialize(statusCodeHandler);
    }
}
