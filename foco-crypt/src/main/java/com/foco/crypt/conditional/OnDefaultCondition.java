package com.foco.crypt.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * TODO
 *
 * @autor 程昭斌
 * @date 2021/4/22 17:33
 */
public class OnDefaultCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotatedTypeMetadata.getAnnotationAttributes(ConditionalOnDefault.class.getName()));
        String name = annotationAttributes.getString("name");
        return environment.getProperty(name)==null;
    }
}
