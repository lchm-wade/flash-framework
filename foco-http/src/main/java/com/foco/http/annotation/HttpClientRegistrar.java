package com.foco.http.annotation;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

@Slf4j
public class HttpClientRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentPostProcessor {
    private ResourceLoader resourceLoader;
    private ClassLoader classLoader;
    private static Environment configurableEnvironment;
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassPathHttpClientScanner scanner = new ClassPathHttpClientScanner(registry, this.classLoader);
        if (this.resourceLoader != null) {
            scanner.setResourceLoader(this.resourceLoader);
        }
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(HttpScan.class.getName()));
        String[] basePackages = annotationAttributes.getStringArray("value");
        boolean configOk=false;
        for(int i=0;i<basePackages.length;i++){
            //支持动态参数获取扫包路径
            basePackages[i] = configurableEnvironment.resolvePlaceholders(basePackages[i]);
            if(!configOk&&StrUtil.isNotBlank(basePackages[i])){
                configOk=true;
            }
        }
        if(!configOk){
            log.warn("http modular not config foco.httpBasePackage,dont scanner .....");
        }
        scanner.registerFilters();
        scanner.doScan(basePackages);
    }
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        configurableEnvironment=environment;
    }
}
