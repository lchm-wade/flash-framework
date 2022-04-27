package com.foco.swagger.autoconfigure;
import com.foco.context.util.ClassUtil;
import com.foco.model.constant.MainClassConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.MappedInterceptor;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author lucoo
 * @version 1.0.0
 * @description 获取所有的AbstractHandlerMapping中的MappedInterceptor将swagger的url全局过滤掉
 * @date 2022/03/11 14:29
 * @since foco2.3.3
 */
@Slf4j
public class IgnoreSwaggerInterceptPostProcessor implements BeanPostProcessor {
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof AbstractHandlerMapping){
            AbstractHandlerMapping abstractHandlerMapping=(AbstractHandlerMapping)bean;
            try {
                Optional<Field> first = Arrays.stream(ClassUtil.getAllFields(abstractHandlerMapping.getClass())).filter((f) -> "adaptedInterceptors".equals(f.getName())).findFirst();
                if(first.isPresent()){
                    Field adaptedInterceptorsField = first.get();
                    adaptedInterceptorsField.setAccessible(true);
                    List<HandlerInterceptor> adaptedInterceptors = (List<HandlerInterceptor>) adaptedInterceptorsField.get(abstractHandlerMapping);
                    MappedInterceptor[] mappedInterceptors = getMappedInterceptors(adaptedInterceptors);
                    excludePatterns(mappedInterceptors);
                }
            } catch (Exception e) {
                log.warn("对所有拦截器全局忽略swagger url失败",e);
            }
        }
        return bean;
    }

    /**
     * 获取所有的MappedInterceptor
     * @param adaptedInterceptors
     * @return
     */
    private final MappedInterceptor[] getMappedInterceptors(List<HandlerInterceptor> adaptedInterceptors) {
        List<MappedInterceptor> mappedInterceptors = new ArrayList(adaptedInterceptors.size());
        for (HandlerInterceptor interceptor : adaptedInterceptors) {
            if (interceptor instanceof MappedInterceptor) {
                mappedInterceptors.add((MappedInterceptor) interceptor);
            }
        }
        return (!mappedInterceptors.isEmpty() ? mappedInterceptors.toArray(new MappedInterceptor[0]) : new MappedInterceptor[0]);
    }
    /**
     * 反射给excludePatterns数组加上swagger的url
     * @return
     */
    private void excludePatterns(MappedInterceptor[] mappedInterceptors){
        for(MappedInterceptor mappedInterceptor:mappedInterceptors){
            try {
                Field excludePatternsField = mappedInterceptor.getClass().getDeclaredField("excludePatterns");
                excludePatternsField.setAccessible(true);

                String[] excludePatterns = (String[])excludePatternsField.get(mappedInterceptor);

                Set<String> excludePatternList = new HashSet<>();

                Collections.addAll(excludePatternList, excludePatterns);
                excludePatternList.addAll(MainClassConstant.SWAGGER_URL);

                excludePatternsField.set(mappedInterceptor,excludePatternList.toArray(new String[0]));
            } catch (Exception e) {
                log.warn("对所有拦截器全局忽略swagger url失败",e);
            }

        }
    }
}
