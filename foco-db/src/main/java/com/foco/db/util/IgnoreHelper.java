package com.foco.db.util;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author lucoo
 * @version 1.0.0
 * @date 2021/11/17 18:02
 * 线程级别忽略表增强
 */
public class IgnoreHelper {
    static ThreadLocal<Boolean> tenantHolder = new InheritableThreadLocal<Boolean>(){
        public Boolean initialValue(){
            return false;
        }
    };
    static ThreadLocal<Boolean> deletedHolder = new InheritableThreadLocal<Boolean>(){
        public Boolean initialValue(){
            return false;
        }
    };
    public static void setTenantFlag(boolean ignore){
        tenantHolder.set(ignore);
    }
    public static void ignoreTenant(){
        tenantHolder.set(true);
    }
    public static boolean getTenant(){
        return tenantHolder.get();
    }
    public static void removeTenant(){
        tenantHolder.remove();
    }

    public static void setDeletedFlag(boolean ignoreDeleted){
        deletedHolder.set(ignoreDeleted);
    }
    public static void ignoreDeleted(){
        deletedHolder.set(true);
    }

    public static boolean getDeleted(){
        return deletedHolder.get();
    }
    public static void removeDeleted(){
        deletedHolder.remove();
    }


    public static <T> T ignoreTenant(Supplier<T> supplier){
            ignoreTenant();
            T t = supplier.get();
            removeTenant();
            return t;
    }
    public static <T,P> T ignoreTenant(P p,Predicate<P> predicate,Supplier<T> supplier){
        if(predicate.test(p)){
            return ignoreTenant(supplier);
        }
        return supplier.get();
    }

    public static <T> T ignoreDelete(Supplier<T> supplier){
        ignoreDeleted();
        T t = supplier.get();
        removeDeleted();
        return t;
    }
    public static <T,P> T ignoreDelete(P p,Predicate<P> predicate,Supplier<T> supplier){
        if(predicate.test(p)){
            return ignoreDelete(supplier);
        }
        return supplier.get();
    }
}
