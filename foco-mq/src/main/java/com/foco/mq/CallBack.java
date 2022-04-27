package com.foco.mq;

import com.foco.mq.model.Msg;

/**
 * @author ChenMing
 * @date 2021/10/14
 */
public interface CallBack {
    /**
     * 回调函数
     * @param success true：成功 false：失败
     * @param msg 消息
     */
    void callBack(boolean success, Msg msg);
}
