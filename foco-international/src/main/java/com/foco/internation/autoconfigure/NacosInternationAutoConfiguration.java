package com.foco.internation.autoconfigure;

import com.foco.internation.parser.PropertiesParser;
import com.foco.internation.parser.naocs.NacosPropertiesParser;
import com.foco.internation.properties.InternationalProperties;
import com.foco.model.constant.MainClassConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/15 10:51
 */
@ConditionalOnClass(name = MainClassConstant.ALIBABA_NACOS_CONFIG)
public class NacosInternationAutoConfiguration {
    @Bean
    @ConditionalOnProperty(prefix= InternationalProperties.PREFIX,name = "type",havingValue = "nacos")
    public PropertiesParser nacosPropertiesParser() {
        return new NacosPropertiesParser();
    }
}
