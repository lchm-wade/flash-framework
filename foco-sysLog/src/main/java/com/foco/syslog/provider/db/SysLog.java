/*
 * PDMS wliduo https://github.com/dolyw
 * Created By generator
 * Date By (2020-12-23 09:30:30)
 */


package com.foco.syslog.provider.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * SysLog
 * @author lucoo
 * @date 2020-12-23 09:30:30
 */
@Data
@ToString(callSuper = true)
@TableName("sys_log")
public class SysLog {

       /** id */
       @TableId("id")
       private Long id;
       @TableField("user_id")
       private String userId;

       /** 用户名 */
       @TableField("user_name")
       private String userName;

       /** 用户操作 */
       @TableField("operation")
       private String operation;

       /** 请求方法 */
       @TableField("method")
       private String method;

       /** 请求参数 */
       @TableField("params")
       private String params;

       /** 执行时长(毫秒) */
       @TableField("time")
       private Long time;

       /** IP地址 */
       @TableField("ip")
       private String ip;

       /** 创建时间 */
       @TableField(value = "create_time", fill = FieldFill.INSERT)
       private java.time.LocalDateTime createTime;


       /** 错误信息 */
       @TableField("error_msg")
       private String errorMsg;

       /** 是否执行成功 1 是 0否 */
       @TableField("status")
       private Boolean status;

       /** 执行方法MD5(后续统计分析可以根据此字段) */
       @TableField("method_md5")
       private String methodMd5;



}