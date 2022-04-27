package com.foco.context.core;

import com.foco.context.executor.ThreadLocalManager;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/11/22 13:37
 */
public class ThreadLocalTransmitManager {
    private ThreadLocalManager manager;
    private FocoContextManager focoContextManager;
    public ThreadLocalTransmitManager() {
        this.manager = new ThreadLocalManager();
        this.focoContextManager=new FocoContextManager();
    }
    public void set(){
        manager.set();
        focoContextManager.set();
    }
    public void remove(){
        manager.remove();
        focoContextManager.remove();
    }
}
