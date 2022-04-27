package com.foco.file.crypt;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.client.NacosPropertySourceLocator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/29 11:31
 */
public class NacosPropertySourceLocatorPostProcessor implements BeanPostProcessor {
    NacosConfigManager nacosConfigManager;
    public NacosPropertySourceLocatorPostProcessor(NacosConfigManager nacosConfigManager) {
        this.nacosConfigManager = nacosConfigManager;
    }
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof NacosPropertySourceLocator){
            return new CustomNacosPropertySourceLocator(nacosConfigManager);
        }
        return bean;
    }
}
