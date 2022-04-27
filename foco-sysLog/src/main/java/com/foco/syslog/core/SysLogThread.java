package com.foco.syslog.core;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author lucoo
 * @date 2021/1/28 16:40
 */
public class SysLogThread {
    @Autowired
    private LogStoreProvider logStoreProvider;
    ThreadPoolExecutor triggerPool;
    public SysLogThread(){
        triggerPool = new ThreadPoolExecutor(
                5,
                30,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000)
                ,((r)-> new Thread(r,"syslog-thread-"+r.hashCode())));
    }
    public void saveSysLog(LogParam logParam){
        triggerPool.submit(()->{
            //保存系统日志
            logStoreProvider.save(logParam);
        });
    }
}
