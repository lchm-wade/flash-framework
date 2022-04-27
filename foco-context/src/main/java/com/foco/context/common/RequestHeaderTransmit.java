package com.foco.context.common;

import java.util.List;

/**
 * @author lucoo
 * @version 1.0.0
 * @description 请求头传递
 * @date 2021/11/20 13:42
 */
public interface RequestHeaderTransmit {
    /**
     * 将需要透传的请求头放在list中
     * @return
     */
    List<String> transmit();
}
