package com.foco.db.scanner;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foco.context.core.SpringContextHolder;
import com.foco.context.util.TypeUtil;
import com.foco.model.constant.FocoConstants;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/12/23 11:46
 * @since foco2.3.0
 */
@Slf4j
public class FocoClassPathScanner implements GenericApplicationListener {
    private AtomicBoolean init = new AtomicBoolean(false);
    int order = Ordered.HIGHEST_PRECEDENCE + 21;
    private String[] locationPatterns=new String[]{
            FocoConstants.MYBATIS_ENTITY_BASE_PACKAGE,
    "classpath*:com/foco/auth/provider/db/*.class",
    "classpath*:com/foco/crypt.core/provider/crypt/db/*.class",
    "classpath*:com/foco/crypt/core/provider/sign/db/*.class",
    "classpath*:com/foco/distributed/id/store/*.class",
    "classpath*:com/foco/mq/model/*.class",
    "classpath*:com/foco/syslog/provider/db/*.class"
    };
    private static final Class<?>[] EVENT_TYPES = {
            ApplicationPreparedEvent.class};

    private static final Class<?>[] SOURCE_TYPES = {SpringApplication.class, ApplicationContext.class};
    private void scanner() throws Exception {

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        for(String locationPattern:locationPatterns){
            Resource[] resources = resolver.getResources(locationPattern);
            for(Resource res :resources) {
                String clsName = new SimpleMetadataReaderFactory().getMetadataReader(res).getClassMetadata().getClassName();
                try {
                    Class<?> clazz = Class.forName(clsName,false,this.getClass().getClassLoader());
                    if(clazz.isAnnotationPresent(TableName.class)){
                        TableName tableName = clazz.getAnnotation(TableName.class);
                        TableIgnoreHandler.addTables(tableName.value());
                    }
                } catch (Exception e) {
                    log.warn("处理foco内部表增强忽略逻辑错误",e);
                }
            }
        }

    }
    public int getOrder(){
        return order;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        try {
            if(init.get()){
                return;
            }
            if(((ApplicationPreparedEvent) event).getApplicationContext().getParent()!=null){
                scanner();
            }
            init.set(true);
        } catch (Exception e) {
            log.error("处理foco内部表增强忽略逻辑异常",e);
        }
    }
    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return isAssignableFrom(sourceType, SOURCE_TYPES);
    }

    private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        if (type != null) {
            for (Class<?> supportedType : supportedTypes) {
                if (supportedType.isAssignableFrom(type)) {
                    return true;
                }
            }
        }
        return false;
    }
}
