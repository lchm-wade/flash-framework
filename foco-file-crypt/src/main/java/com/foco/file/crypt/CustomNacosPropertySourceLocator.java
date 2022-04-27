package com.foco.file.crypt;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/10 15:34
 */
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.client.NacosPropertySource;
import com.alibaba.cloud.nacos.client.NacosPropertySourceLocator;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @Description: TODO
 * @Author lucoo
 * @Date 2021/6/31 18:09
 **/
public class CustomNacosPropertySourceLocator extends NacosPropertySourceLocator {
    public CustomNacosPropertySourceLocator(NacosConfigManager nacosConfigManager) {
        super(nacosConfigManager);
    }
    public PropertySource<?> locate(Environment env){
        CompositePropertySource compositePropertySource =(CompositePropertySource) super.locate(env);
        String key=env.getProperty("foco.password.key");
        if(!StringUtils.isEmpty(key)){
            Collection<PropertySource<?>> propertySources = compositePropertySource.getPropertySources();
            for(PropertySource propertySource:propertySources){
                NacosPropertySource nacosPropertySource=(NacosPropertySource) propertySource;
                String[] propertyNames = nacosPropertySource.getPropertyNames();
                for(String name:propertyNames){
                    Object value = nacosPropertySource.getProperty(name);
                    if(value instanceof String&&((String) value).startsWith("ENC:")){
                        String finalValue;
                        try {
                            String val = ((String) value).substring(4);
                            finalValue = AesUtil.decrypt(val,key);
                            nacosPropertySource.getSource().put(name, finalValue);
                        } catch (Exception e) {
                            throw new IllegalArgumentException(name+"值解密异常,请检查是否错误");
                        }
                    }
                }
            }
        }
        return compositePropertySource;
    }
}
