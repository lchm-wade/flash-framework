package com.foco.context.executor;

import com.foco.context.core.SpringContextHolder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 线程池中 线程上下文传递管理
 * @Author lucoo
 * @Date 2021/6/10 14:02
 **/
public class ThreadLocalManager {
    private static Collection<ThreadLocalTransmit> threadLocalTransmits = SpringContextHolder.getBeansOfType(ThreadLocalTransmit.class);
    Map<Class<? extends ThreadLocalTransmit>,Object> threadLocalValue=new HashMap<>();
    public ThreadLocalManager(){
        for(ThreadLocalTransmit help: threadLocalTransmits){
            Object o = help.get();
            threadLocalValue.put(help.getClass(),o);
        }
    }
    public void set(){
        for(ThreadLocalTransmit help: threadLocalTransmits) {
            help.set(threadLocalValue.get(help.getClass()));
        }
    }
    public void remove(){
        for(ThreadLocalTransmit help: threadLocalTransmits) {
            help.remove();
        }
    }
}
