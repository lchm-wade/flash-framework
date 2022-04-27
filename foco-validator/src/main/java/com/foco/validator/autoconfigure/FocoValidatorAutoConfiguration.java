package com.foco.validator.autoconfigure;

import com.foco.context.util.BootStrapPrinter;
import com.foco.model.constant.FocoConstants;
import com.foco.validator.custom.StatusCodeChecker;
import com.foco.validator.validation.ValidAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-06-29 14:48
 */
@EnableConfigurationProperties(ValidatorProperties.class)
@Configuration
@ConditionalOnProperty(prefix = ValidatorProperties.PREFIX,name = FocoConstants.ENABLED,matchIfMissing = true)
public class FocoValidatorAutoConfiguration {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-validator",this.getClass());
    }
    @Bean
    ValidAspect validAspect(){
        return new ValidAspect();
    }
    @Bean
    StatusCodeChecker statusCodeChecker(){
        return new StatusCodeChecker();
    }
}
