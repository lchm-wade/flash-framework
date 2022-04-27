package com.foco.mq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foco.db.annotation.MultiIgnoreLogicDelete;
import com.foco.mq.model.SendRecord;

/**
 * @author ChenMing
 * @date 2021/10/14
 */
@MultiIgnoreLogicDelete({"delete", "update", "selectOne"})
public interface SendRecordMapper extends BaseMapper<SendRecord> {
}