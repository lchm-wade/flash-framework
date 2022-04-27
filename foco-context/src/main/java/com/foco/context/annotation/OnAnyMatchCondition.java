package com.foco.context.annotation;

import cn.hutool.core.util.StrUtil;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Locale;

/**
 * TODO
 *
 * @autor 程昭斌
 * @date 2021/4/22 17:33
 */
public class OnAnyMatchCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotatedTypeMetadata.getAnnotationAttributes(ConditionalOnAnyMatch.class.getName()));
        String[] names = annotationAttributes.getStringArray("name");
        String[] values = annotationAttributes.getStringArray("value");
        boolean flage;
        for (int i = 0; i < names.length; i++) {
            String property = environment.getProperty(names[i]);
            if(StrUtil.isNotBlank(property)){
                flage = property.toUpperCase(Locale.ROOT).equals(values[i].toUpperCase(Locale.ROOT));
                if (flage) {
                    return true;
                }
            }
        }
        return false;
    }
}
