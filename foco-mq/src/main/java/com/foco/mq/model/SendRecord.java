package com.foco.mq.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ChenMing
 * @date 2021/10/14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value="`send_record`")
public class SendRecord implements Serializable{

    private static final long serialVersionUID = 1L;

    /** 消息类型 0：消费端产生数据 1：生产端产生数据*/
    @TableField(value = "`type`")
    private Integer type;

    /** 消息主题 */
    @TableField(value = "`topic`")
    private String topic;

    /** 发送时当前上下文请求头 */
    @TableField(value = "`headers`")
    private String headers;

    /** Msg的properties */
    @TableField(value = "`properties`")
    private String properties;

    /** 业务性唯一标志，如不用无需设值*/
    @TableField(value = "`keys`")
    private String keys;

    /** 推送失败原因信息 */
    @TableField(value = "`error_msg`")
    private String errorMsg;

    /** 最大重推次数 默认次数请看数据库表设计*/
    @TableField(value = "`max_count`")
    private Integer maxCount;

    /** 当前推送次数 默认0*/
    @TableField(value = "`current_count`")
    private Integer currentCount;

    /** 消息内容 */
    @TableField(value = "`msg_body`")
    private byte[] msgBody;

    /** 唯一标识符，集群模式为“消费组”，广播模式为“ip+port” */
    @TableField(value = "`unique_identifier`")
    private String uniqueIdentifier;

    /** 是否删除 0否1是 */
    @TableField(value = "`del_flag`")
    private Integer delFlag;

    /** 创建时间 */
    @TableField(value = "`create_time`")
    private Date createTime;

    /** 修改时间 */
    @TableField(value = "`modify_time`")
    private Date modifyTime;

	
}