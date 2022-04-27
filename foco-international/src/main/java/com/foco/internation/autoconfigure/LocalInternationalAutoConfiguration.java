package com.foco.internation.autoconfigure;

import com.foco.internation.parser.LocalFilePropertiesParser;
import com.foco.internation.parser.PropertiesParser;
import com.foco.internation.parser.naocs.NacosPropertiesParser;
import com.foco.internation.properties.InternationalProperties;
import com.foco.internation.resolver.SimpleMessageResolver;
import com.foco.model.constant.FocoConstants;
import com.foco.model.constant.MainClassConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-06-29 14:48
 */
public class LocalInternationalAutoConfiguration {
    @Bean
    @ConditionalOnProperty(prefix=InternationalProperties.PREFIX,name = "type",havingValue = "local",matchIfMissing = true)
    public PropertiesParser propertiesResolver() {
        return new LocalFilePropertiesParser();
    }

    @Bean
    SimpleMessageResolver simpleMessageResolver(){
        return new SimpleMessageResolver();
    }
}
