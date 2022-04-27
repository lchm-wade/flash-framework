package com.foco.mq.model;

import lombok.Data;

/**
 * @author ChenMing
 * @date 2021/10/14
 */
@Data
public class SendResult {
    /**
     * true：成功 false：失败
     */
    private boolean succeed;

    /**
     * 框架原生数据体
     */
    private Object result;
}
