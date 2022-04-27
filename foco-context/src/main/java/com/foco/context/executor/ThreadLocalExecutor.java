package com.foco.context.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/5 10:42
 **/
public interface ThreadLocalExecutor extends Executor {
    void execute(Runnable command);
    Future<?> submit(Runnable runnable);
    <T> Future<T> submit(Callable<T> callable);
}
