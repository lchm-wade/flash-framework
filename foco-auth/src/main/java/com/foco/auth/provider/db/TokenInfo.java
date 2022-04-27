/*
 * PDMS wliduo https://github.com/dolyw
 * Created By generator
 * Date By (2020-12-03 10:57:42)
 */


package com.foco.auth.provider.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * TokenInfo
 * @author lucoo
 * @date 2020-12-03 10:57:42
 */
@Data
@ToString(callSuper = true)
@TableName("sys_token_info")
public class TokenInfo {

       /** id */
       @TableId("id")
       private Long id;


       /** 用户id */
       @TableField("user_id")
       private String userId;

       /** 客户端类型 */
       @TableField("client_type")
       private String clientType;

       /** 网络类型 */
       @TableField("network_type")
       private String networkType;

       /** token */
       @TableField("token")
       private String token;

       /** token过期时间 */
       @TableField("expire_time")
       private LocalDateTime expireTime;

       /** 创建时间 */
       @TableField(value = "create_time", fill = FieldFill.INSERT)
       private LocalDateTime createTime;
       /** 更新时间 */
       @TableField(value = "modify_time", fill = FieldFill.INSERT_UPDATE)
       private LocalDateTime modifyTime;
       /** 用户信息 json格式 */
       @TableField("user_info")
       private String userInfo;
}