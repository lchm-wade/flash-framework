package com.foco.file.crypt;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/10 15:34
 */

import com.alibaba.cloud.nacos.NacosConfigManager;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/08/02 11:50
 */
@ConditionalOnClass(name="com.alibaba.cloud.nacos.NacosConfigAutoConfiguration")
public class CustomNacosConfigBootstrapConfiguration implements BeanPostProcessor {
   @Bean
    NacosPropertySourceLocatorPostProcessor nacosPropertySourceLocatorPostProcessor(NacosConfigManager nacosConfigManager){
       return new NacosPropertySourceLocatorPostProcessor(nacosConfigManager);
   }
}

