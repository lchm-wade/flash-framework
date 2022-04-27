package com.foco.context.executor;

/**
 * @Description 线程上下文传递
 * @Author lucoo
 * @Date 2021/6/10 13:56
 **/
public interface ThreadLocalTransmit<T> {
     /**
      * 将对象放置到自己的ThreadLocal中
      * @param t
      */
     void set(T t);
     /**
      * 返回ThreadLocal存储的变量
      */
     T get();

    /**
     * 移除变量
      */
     void remove();
}
