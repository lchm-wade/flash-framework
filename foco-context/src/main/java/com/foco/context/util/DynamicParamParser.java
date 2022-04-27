package com.foco.context.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 * 处理注解内部的的动态参数
 * 写在方法上,会从方法参数上去拿对应的参数值
 * @xxAnnotation("xx${name}yy${mm}-${user.age}")
 * public void test(String age,String name,String mm,User user){}
 * 其中
 * User{
 * private String name;
 * private String age;
 * }
 * 如上，会直接从第一个参数开始获取${name}的值,如诺不空则直接返回取到的值，否则继续往下找
 * @Author lucoo
 * @Date 2021/6/24 11:16
 */
@Slf4j
public class DynamicParamParser {
    static LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    static ExpressionParser parser = new SpelExpressionParser();
    private final static String PREFIX = "${";
    private final static String SUFFIX = "}";
    private final static Pattern REGEX = Pattern.compile("\\$\\{([^}]*)\\}");

    private static boolean isBaseType(Class className, boolean isBaseStr) {
        if (isBaseStr && className.equals(String.class)) {
            return true;
        }
        return className.equals(Integer.class) ||
                className.equals(int.class) ||
                className.equals(Byte.class) ||
                className.equals(byte.class) ||
                className.equals(Long.class) ||
                className.equals(long.class) ||
                className.equals(Double.class) ||
                className.equals(double.class) ||
                className.equals(Float.class) ||
                className.equals(float.class) ||
                className.equals(Character.class) ||
                className.equals(char.class) ||
                className.equals(Short.class) ||
                className.equals(short.class) ||
                className.equals(Boolean.class) ||
                className.equals(boolean.class);
    }

    public static boolean isDynameicParam(String var) {
        return var.contains(PREFIX);
    }

    public static String handle(Method method, Object[] args, String var) {
        if (method == null || args == null || StrUtil.isBlank(var)) {
            return "";
        }
        if (!isDynameicParam(var)) {
            //不是动态参数,直接原样返回
            return var;
        }
        log.info("处理前的动态参数:{}", var);
        Matcher matcher = REGEX.matcher(var);
        List<String> varList = new ArrayList<>();
        List<Object> valList = new ArrayList<>();
        while (matcher.find()) {
            varList.add(matcher.group(1));
        }
        for (String v : varList) {
            Object o = parserVar(method, args, v);
            if (o == null) {
                //若没有对应取值，则用原串替换
                valList.add(v);
            } else {
                valList.add(o);
            }
        }
        String pattern = var.replaceAll(REGEX.toString(), "%s");
        String resolvedVar = String.format(pattern, valList.toArray());
        log.info("处理后的动态参数:{}", resolvedVar);
        return resolvedVar;
    }

    private static Object parserVar(Method method, Object[] objects, String var) {
        Class[] parameterTypes = method.getParameterTypes();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Object parsedVar = null;
        for (int i = 0; i < parameterTypes.length; i++) {
            Class clazz = parameterTypes[i];
            String parameterName = parameterNames[i];
            if (isBaseType(clazz, true)) {
                if (parameterName.equals(var)) {
                    parsedVar = objects[i];
                    if (parsedVar != null) {
                        break;
                    }
                }
            } else {
                String[] split = var.split("\\.");
                if(split.length>1&&split[0].equals(parameterName)){
                    var = var.substring(parameterName.length() + 1);
                    parsedVar = handCustomerObj(objects[i], var);
                }
                if (parsedVar != null) {
                    break;
                }
            }
        }
        return parsedVar;
    }

    private static Object handCustomerObj(Object customerObj, String var) {
        if(customerObj instanceof List){
            customerObj=((List)customerObj).get(0);
        }
        String[] split = var.split("\\.");
        if (split.length > 1) {
            try {
                Field declaredField = customerObj.getClass().getDeclaredField(split[0]);
                declaredField.setAccessible(true);
                Object o = declaredField.get(customerObj);
                var = var.substring(split[0].length()+1);
                return handCustomerObj(o, var);
            } catch (Exception e) {
            }
        }
        //处理自定义类型
        Object val = null;
        Field[] declaredFields = ClassUtil.getAllFields(customerObj.getClass());
        for (Field field : declaredFields) {
            if (field.getName().equals(var)) {
                field.setAccessible(true);
                try {
                    val = field.get(customerObj);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return val;
    }

    public static String parse(Method method, Object[] args, String key) {
        if (!key.contains("#")) {
            return key;
        }
        String[] params = parameterNameDiscoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }
}
