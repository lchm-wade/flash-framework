package com.foco.context.util;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *     1.BeanCopier: cglib的bean 拷贝 性能比BeanUtil更好
 *     2.ReflectASM 使用字节码生成的方式实现了更为高效的反射机制，不依赖于 Java 本身的反射机制来实现的，
 *     并且避免了访问原始类型因自动装箱而产生的问题。
 * </p>
 * @Author lucoo
 * @Date 2021/6/28 11:16
 */
public class BeanCopierEx {
    /***缓存拷贝类的BeanCopier*/
    private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();
    /***缓存拷贝类的反射信息*/
    private static final Map<String, ConstructorAccess> CONSTRUCTOR_ACCESS_CACHE = new ConcurrentHashMap<>();

    /**
     *深拷贝
     */
    public static<T> T deepClone(T src){
        Object obj;
        ByteArrayOutputStream byteArrayOutputStream=null;
        ObjectOutputStream objectOutputStream=null;
        ByteArrayInputStream byteArrayInputStream=null;
        ObjectInputStream objectInputStream=null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(src);

            byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            obj = objectInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Create new instance of %s failed: %s", src, e.getMessage()),e);
        }finally {
            if(objectOutputStream!=null){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(byteArrayOutputStream!=null){
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(objectInputStream!=null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(byteArrayInputStream!=null){
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return (T) obj;
    }

    public static void copyProperties(Object source, Object target) {
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass());
        copier.copy(source, target, null);
    }

    private static BeanCopier getBeanCopier(Class sourceClass, Class targetClass) {
        String beanKey = generateKey(sourceClass, targetClass);
        BeanCopier copier;
        if (!BEAN_COPIER_CACHE.containsKey(beanKey)) {
            copier = BeanCopier.create(sourceClass, targetClass, false);
            BEAN_COPIER_CACHE.put(beanKey, copier);
        } else {
            copier = BEAN_COPIER_CACHE.get(beanKey);
        }
        return copier;
    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }

    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        if (source==null) {
            return null;
        }
        T t;
        try {
            t = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(String.format("Create new instance of %s failed: %s", targetClass, e.getMessage()));
        }
        copyProperties(source, t);
        return t;
    }

    public static <T> List<T> copyPropertiesOfList(List<?> sourceList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        ConstructorAccess<T> constructorAccess = getConstructorAccess(targetClass);
        List<T> resultList = new ArrayList<>(sourceList.size());
        for (Object o : sourceList) {
            T t;
            try {
                t = constructorAccess.newInstance();
                copyProperties(o, t);
                resultList.add(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return resultList;
    }

    private static <T> ConstructorAccess<T> getConstructorAccess(Class<T> targetClass) {
        ConstructorAccess<T> constructorAccess =CONSTRUCTOR_ACCESS_CACHE.get(targetClass.toString());
        if(constructorAccess != null) {
            return constructorAccess;
        }
        try {
            constructorAccess = ConstructorAccess.get(targetClass);
            constructorAccess.newInstance();
            CONSTRUCTOR_ACCESS_CACHE.put(targetClass.toString(),constructorAccess);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Create new instance of %s failed: %s", targetClass, e.getMessage()));
        }
        return constructorAccess;
    }
}
