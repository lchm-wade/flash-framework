package com.foco.http.annotation;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/10 16:59
 **/

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class ClassPathHttpClientScanner extends ClassPathBeanDefinitionScanner {
    private final ClassLoader classLoader;
    private static final Logger logger = LoggerFactory.getLogger(com.github.lianjiatech.retrofit.spring.boot.core.ClassPathRetrofitClientScanner.class);

    public ClassPathHttpClientScanner(BeanDefinitionRegistry registry, ClassLoader classLoader) {
        super(registry, false);
        this.classLoader = classLoader;
    }

    public void registerFilters() {
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(RetrofitClient.class);
        this.addIncludeFilter(annotationTypeFilter);
    }

    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            logger.warn("No RetrofitClient was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            this.processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        if (beanDefinition.getMetadata().isInterface()) {
            try {
                Class<?> target = ClassUtils.forName(beanDefinition.getMetadata().getClassName(), this.classLoader);
                return !target.isAnnotation();
            } catch (Exception var3) {
                logger.error("load class exception:", var3);
            }
        }

        return false;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        Iterator var3 = beanDefinitions.iterator();

        while(var3.hasNext()) {
            BeanDefinitionHolder holder = (BeanDefinitionHolder)var3.next();
            GenericBeanDefinition definition = (GenericBeanDefinition)holder.getBeanDefinition();
            if (logger.isDebugEnabled()) {
                logger.debug("Creating RetrofitClientBean with name '" + holder.getBeanName() + "' and '" + definition.getBeanClassName() + "' Interface");
            }

            definition.getConstructorArgumentValues().addGenericArgumentValue(Objects.requireNonNull(definition.getBeanClassName()));
            definition.setBeanClass(RetrofitFactoryBean.class);
        }

    }
}

