package com.foco.context.annotation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionMessage.Style;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * {@link Condition} that checks if properties are defined in environment.
 *
 * @author Maciej Walkowiak
 * @author Phillip Webb
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 * @see ConditionalOnPropertyFoco
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 40)
class OnPropertyCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        List<AnnotationAttributes> allAnnotationAttributes = annotationAttributesFromMultiValueMap(
                metadata.getAllAnnotationAttributes(ConditionalOnPropertyFoco.class.getName()));
        List<ConditionMessage> noMatch = new ArrayList<>();
        List<ConditionMessage> match = new ArrayList<>();
        for (AnnotationAttributes annotationAttributes : allAnnotationAttributes) {
            ConditionOutcome outcome = determineOutcome(annotationAttributes, context.getEnvironment());
            (outcome.isMatch() ? match : noMatch).add(outcome.getConditionMessage());
        }
        if (!noMatch.isEmpty()) {
            return ConditionOutcome.noMatch(ConditionMessage.of(noMatch));
        }
        return ConditionOutcome.match(ConditionMessage.of(match));
    }

    private List<AnnotationAttributes> annotationAttributesFromMultiValueMap(
            MultiValueMap<String, Object> multiValueMap) {
        List<Map<String, Object>> maps = new ArrayList<>();
        multiValueMap.forEach((key, value) -> {
            for (int i = 0; i < value.size(); i++) {
                Map<String, Object> map;
                if (i < maps.size()) {
                    map = maps.get(i);
                } else {
                    map = new HashMap<>();
                    maps.add(map);
                }
                map.put(key, value.get(i));
            }
        });
        List<AnnotationAttributes> annotationAttributes = new ArrayList<>(maps.size());
        for (Map<String, Object> map : maps) {
            annotationAttributes.add(AnnotationAttributes.fromMap(map));
        }
        return annotationAttributes;
    }

    private ConditionOutcome determineOutcome(AnnotationAttributes annotationAttributes, PropertyResolver resolver) {
        OnPropertyCondition.Spec spec = new OnPropertyCondition.Spec(annotationAttributes);
        List<String> missingProperties = new ArrayList<>();
        List<String> nonMatchingProperties = new ArrayList<>();
        spec.collectProperties(resolver, missingProperties, nonMatchingProperties);
        if (!missingProperties.isEmpty()) {
            return ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnPropertyFoco.class, spec)
                    .didNotFind("property", "properties").items(Style.QUOTE, missingProperties));
        }
        if (!nonMatchingProperties.isEmpty()) {
            return ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnPropertyFoco.class, spec)
                    .found("different value in property", "different value in properties")
                    .items(Style.QUOTE, nonMatchingProperties));
        }
        return ConditionOutcome
                .match(ConditionMessage.forCondition(ConditionalOnPropertyFoco.class, spec).because("matched"));
    }

    private static class Spec {

        private final String prefix;

        private final String havingValue;

        private final String[] names;

        private final boolean matchIfMissing;

        Spec(AnnotationAttributes annotationAttributes) {
            String prefix = annotationAttributes.getString("prefix").trim();
            if (StringUtils.hasText(prefix) && !prefix.endsWith(".")) {
                prefix = prefix + ".";
            }
            this.prefix = prefix;
            this.havingValue = annotationAttributes.getString("havingValue");
            this.names = getNames(annotationAttributes);
            this.matchIfMissing = annotationAttributes.getBoolean("matchIfMissing");
        }

        private String[] getNames(Map<String, Object> annotationAttributes) {
            String[] value = (String[]) annotationAttributes.get("value");
            String[] name = (String[]) annotationAttributes.get("name");
            Assert.state(value.length > 0 || name.length > 0,
                    "The name or value attribute of @ConditionalOnProperty must be specified");
            Assert.state(value.length == 0 || name.length == 0,
                    "The name and value attributes of @ConditionalOnProperty are exclusive");
            return (value.length > 0) ? value : name;
        }

        private void collectProperties(PropertyResolver resolver, List<String> missing, List<String> nonMatching) {
            for (String name : this.names) {
                String key = this.prefix + name;
                boolean containsProperty = resolver.containsProperty(key);
                String convertKey = convert(key);
                if (!containsProperty) {
                    containsProperty = resolver.containsProperty(convertKey);
                }
                if (containsProperty) {
                    String property = resolver.getProperty(key);
                    if (StringUtils.isEmpty(property)) {
                        property = resolver.getProperty(convertKey);
                    }
                    if (!isMatch(property, this.havingValue)) {
                        nonMatching.add(name);
                    }
                } else {
                    if (!this.matchIfMissing) {
                        missing.add(name);
                    }
                }
            }
        }

        private String convert(String key) {
            char[] charArray = key.toCharArray();
            StringBuilder result = new StringBuilder();
            for (char c : charArray) {
                if (c >= 'A' && c <= 'Z') {
                    result.append("-").append(Character.toLowerCase(c));
                } else {
                    result.append(c);
                }
            }
            return result.toString();
        }

        private boolean isMatch(String value, String requiredValue) {
            if (StringUtils.hasLength(requiredValue)) {
                return requiredValue.equalsIgnoreCase(value);
            }
            return !"false".equalsIgnoreCase(value);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("(");
            result.append(this.prefix);
            if (this.names.length == 1) {
                result.append(this.names[0]);
            } else {
                result.append("[");
                result.append(StringUtils.arrayToCommaDelimitedString(this.names));
                result.append("]");
            }
            if (StringUtils.hasLength(this.havingValue)) {
                result.append("=").append(this.havingValue);
            }
            result.append(")");
            return result.toString();
        }

    }

}

