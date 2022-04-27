package com.foco.distributed.id.store;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/08/27 09:59
 */
@Data
@ToString(callSuper = true)
@TableName("t_segment")
public class Segment {
        @TableId("id")
        private Integer id;
        @TableField("biz_tag")
        private String bizTag;
        /**初始值*/
        @TableField("init_val")
        private Long initVal;
        /**重置的时间*/
        @TableField("reset_time")
        private String resetTime;
        /**当前值*/
        @TableField("current_val")
        private Long currentVal;
        /**步长*/
        @TableField("step")
        private Integer step;
        /** 创建时间 */
        @TableField(value = "create_time", fill = FieldFill.INSERT)
        private LocalDateTime createTime;
        /** 更新时间 */
        @TableField(value = "modify_time", fill = FieldFill.INSERT_UPDATE)
        private LocalDateTime modifyTime;
        @TableField("bizDescribe")
        private String biz_describe;
}
