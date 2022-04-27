package com.foco.auth.condition;

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
public class OnAuthCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotatedTypeMetadata.getAnnotationAttributes(ConditionalOnAuth.class.getName()));
        String value = annotationAttributes.getString("value");
        return value.equals(environment.getProperty("foco.token.token-type"));
    }
}
