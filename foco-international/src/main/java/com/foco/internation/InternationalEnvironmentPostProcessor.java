package com.foco.internation;
import com.foco.model.constant.FocoConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @Description 读取配置文件放入environment中, 作为异常码的资源
 * @Author lucoo
 * @Date 2021/6/13 15:07
 **/
@Slf4j
public class InternationalEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        handlerErrorCode(environment);
        handlerStatusCode(environment);
    }
    private void handlerStatusCode(ConfigurableEnvironment environment) {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:status/*.properties");
            for (Resource resource : resources) {
                Properties properties = new Properties();
                properties.load(new BufferedReader(new InputStreamReader(resource.getInputStream())));

                PropertiesPropertySource propertySource = new PropertiesPropertySource(FocoConstants.STATUS_CODE_FILE+resource.getFilename(), properties);
                environment.getPropertySources().addLast(propertySource);
                //翻转properties
                Enumeration<?> enumeration = properties.propertyNames();
                Properties propertiesRevers = new Properties();
                while (enumeration.hasMoreElements()){
                    Object key = enumeration.nextElement();
                    String value = properties.getProperty(String.valueOf(key));
                    propertiesRevers.put(value,key);
                }
                PropertiesPropertySource propertySourceValue = new PropertiesPropertySource(FocoConstants.STATUS_CODE_REVERS_FILE+resource.getFilename(), propertiesRevers);
                environment.getPropertySources().addLast(propertySourceValue);
            }
        } catch (IOException e) {
            log.warn("未读取到状态码配置文件");
        } catch (Exception e) {
            log.error("读取状态码配置文件异常", e);
        }
    }
    private void handlerErrorCode(ConfigurableEnvironment environment) {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:error/*.properties");
            for (Resource resource : resources) {
                Properties properties = new Properties();
                properties.load(new BufferedReader(new InputStreamReader(resource.getInputStream())));
                PropertiesPropertySource propertySource = new PropertiesPropertySource(FocoConstants.ERROR_CODE_FILE+resource.getFilename(), properties);
                environment.getPropertySources().addLast(propertySource);
            }
        } catch (IOException e) {
            log.warn("未读取到异常码配置文件");
        } catch (Exception e) {
            log.error("读取异常码配置文件异常", e);
        }
    }
}
