package com.foco.context.util;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.ResolvableType;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description 获取子类泛型类型
 * public class A extend SupperClass<String>{
 * <p>
 * }
 * 那么可以调用TypeUtil.getFirstModelBySupperClass(new A())获取得到String.class(指的是父类的泛型)
 *
 * * public class B implement SupperInterface<Integer>{
 *  * <p>
 *  * }
 *  * 那么可以调用TypeUtil.getFirstModelBySupperInterface(new B())获取得到Integer.class(指的是父接口的泛型)
 * @date 2021-07-05 11:54
 */
public class TypeUtil {
    public static Class getFirstModelBySupperInterface(Object obj, Class supperInterface) {
        Class[] model = getModelBySupperInterface(obj, supperInterface);
        return model==null?null:model[0];
    }
    public static Class[] getModelBySupperInterface(Object obj, Class supperInterface) {
        Class<?> targetClass = AopUtils.getTargetClass(obj);
        ResolvableType[] interfaces = ResolvableType.forClass(targetClass).getInterfaces();
        for (ResolvableType type : interfaces) {
            if (supperInterface.isAssignableFrom(type.getRawClass())) {
                ResolvableType[] generics = type.getGenerics();
                Class[] classes=new Class[generics.length];
                for(int i=0;i<generics.length;i++){
                    classes[i]=generics[i].resolve();
                }
                return classes;
            }
        }
        return null;
    }

    public static Class getFirstModelBySupperClass(Object obj) {
        Class[] model = getModelBySupperClass(obj);
        return model==null?null:model[0];
    }
    public static Class[] getModelBySupperClass(Object obj) {
        Class<?> targetClass = AopUtils.getTargetClass(obj);
        ResolvableType type = ResolvableType.forClass(targetClass).getSuperType();
        ResolvableType[] generics = type.getGenerics();
        if(generics.length!=0){
            Class[] classes=new Class[generics.length];
            for(int i=0;i<generics.length;i++){
                classes[i]=generics[i].resolve();
            }
            return classes;
        }
        return null;
    }
}
