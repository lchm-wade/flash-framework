package com.foco.syslog.core;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/22 19:04
 **/
@Getter
@Setter
public class LogParam {
    private String userId;
    //用户名
    private String userName;
    //用户操作
    private String operation;
    //请求方法
    private String method;
    //请求参数
    private String params;
    //执行时长(毫秒)
    private Long time;
    //IP地址
    private String ip;
    /** 错误信息 */
    private String errorMsg;

    /** 是否执行成功 1 是 0否 */
    private Boolean status;

    /** 执行方法MD5(后续统计分析可以根据此字段) */
    private String methodMd5;
}
