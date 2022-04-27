/*
 * PDMS wliduo https://github.com/dolyw
 * Created By generator
 * Date By (2020-12-16 15:22:50)
 */


package com.foco.crypt.core.provider.sign.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * SysCryptInfo
 * @author lucoo
 * @date 2020-12-16 15:22:50
 */
@Data
@ToString(callSuper = true)
@TableName("sys_sign_info")
public class SignInfo {
       /** id */
       @TableId("id")
       private Integer id;


       /** 渠道应用id */
       @TableField("app_id")
       private String appId;
       /** 加签的秘钥 */
       @TableField("sign_key")
       private String signKey;
       /** 创建时间 */
       @TableField(value = "create_time", fill = FieldFill.INSERT)
       private java.time.LocalDateTime createTime;


       /** 更新时间 */
       @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
       private java.time.LocalDateTime updateTime;
       /** 非对称加密模式下 对端公钥 */
       @TableField("sign_party_public_key")
       private String signPartyPublicKey;

       /** 非对称加密模式下 平台私钥 */
       @TableField("sign_platform_private_key")
       private String signPlatformPrivateKey;

       /** 非对称加密模式下 平台公钥(下发给渠道放) */
       @TableField("sign_platform_public_key")
       private String signPlatformPublicKey;
}