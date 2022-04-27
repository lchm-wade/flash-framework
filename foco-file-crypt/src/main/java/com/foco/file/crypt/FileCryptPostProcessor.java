package com.foco.file.crypt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * @Description 读取配置文件放入environment中, 作为异常码的资源
 * @Author lucoo
 * @Date 2021/6/13 15:07
 **/
public class FileCryptPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        handlerCrypt(environment);

    }
    public void handlerCrypt(ConfigurableEnvironment environment) {
        MutablePropertySources mutablePropertySources = environment.getPropertySources();
        String key = environment.getProperty("foco.password.key");
        if (!StringUtils.isEmpty(key)) {
            HashMap<String, Object> map = new HashMap<>();
            for (PropertySource propertySource : mutablePropertySources) {
                if (ClassUtils.isPresent("org.springframework.boot.env.OriginTrackedMapPropertySource",this.getClass().getClassLoader())
                        &&propertySource instanceof OriginTrackedMapPropertySource) {
                    OriginTrackedMapPropertySource originPropertySource = (OriginTrackedMapPropertySource) propertySource;
                    String[] propertyNames = originPropertySource.getPropertyNames();
                    for (String propertyName : propertyNames) {
                        Object value = originPropertySource.getProperty(propertyName);
                        if (value instanceof String && ((String) value).startsWith("ENC:")) {
                            String finalValue;
                            try {
                                String val = ((String) value).substring(4);
                                finalValue = AesUtil.decrypt(val,key);
                            } catch (Exception e) {
                                throw new IllegalArgumentException(propertyName + "值解密异常,请检查是否错误",e);
                            }
                            map.put(propertyName, finalValue);
                        }
                    }
                }
            }
            if (!map.isEmpty()) {
                environment.getPropertySources().addFirst(new MapPropertySource("foco-password-encrypt", map));
            }
        }
        //排除nacos自动装配
        /*HashMap<String, Object> excludeNacos = new HashMap<>();
        excludeNacos.put("spring.cloud.nacos.config.enabled","false");
        environment.getPropertySources().addFirst(new MapPropertySource("foco-cloud-excludeNacos-encrypt", excludeNacos));*/
    }
}
