package com.foco.swagger.autoconfigure;

import com.foco.model.constant.FocoConstants;
import com.foco.model.constant.MainClassConstant;
import com.foco.swagger.gateway.CompositeSwaggerResource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/11 14:55
 */
@ConditionalOnClass(name = MainClassConstant.SPRING_CLOUD_GATEWAY)
@ComponentScan(FocoConstants.MAIN_PACKAGE_SWAGGER)
public class GatewaySwaggerAutoConfiguration {
    @Bean
    CompositeSwaggerResource compositeSwaggerResource(){
        return new CompositeSwaggerResource();
    }
}
