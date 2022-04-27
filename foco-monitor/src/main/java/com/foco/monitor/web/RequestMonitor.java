package com.foco.monitor.web;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/20 14:39
 */
@Getter
@Setter
public class RequestMonitor {
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 接口路径
     */
    private String path;
    /**
     * get post
     */
    private String method;
    /**
     * http状态码 200 400 500
     */
    private String code;
    /**
     * 接口耗时
     */
    private String costTimeMs;
}
