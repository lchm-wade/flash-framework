package com.foco.page.mvc;


import com.foco.context.executor.ThreadLocalTransmit;
import com.foco.model.page.PageParam;
import com.foco.model.page.ThreadPagingUtil;


/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/10 14:18
 **/
public class PageContextTransmit implements ThreadLocalTransmit<PageParam> {
    @Override
    public void set(PageParam pageParam) {
        ThreadPagingUtil.set(pageParam);
    }
    @Override
    public PageParam get() {
        return ThreadPagingUtil.get();
    }

    @Override
    public void remove() {
        ThreadPagingUtil.clear();
    }
}
