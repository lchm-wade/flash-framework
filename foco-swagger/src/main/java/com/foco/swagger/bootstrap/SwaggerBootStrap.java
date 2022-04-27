package com.foco.swagger.bootstrap;

import com.foco.context.util.BootStrapPrinter;
import com.foco.model.constant.FocoConstants;
import com.foco.swagger.autoconfigure.GatewaySwaggerAutoConfiguration;
import com.foco.swagger.autoconfigure.IgnoreSwaggerInterceptPostProcessor;
import com.foco.swagger.autoconfigure.SwaggerAutoConfiguration;
import com.foco.swagger.properties.SwaggerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/11 15:01
 */
@ConditionalOnProperty(prefix = SwaggerProperties.PREFIX,name = FocoConstants.ENABLED)
@Import({SwaggerAutoConfiguration.class, GatewaySwaggerAutoConfiguration.class})
public class SwaggerBootStrap {
    @PostConstruct
    public void init(){
        BootStrapPrinter.log("foco-swagger",this.getClass());
    }
    @Bean
    IgnoreSwaggerInterceptPostProcessor ignoreSwaggerInterceptPostProcessor(){
        return new IgnoreSwaggerInterceptPostProcessor();
    }
}
