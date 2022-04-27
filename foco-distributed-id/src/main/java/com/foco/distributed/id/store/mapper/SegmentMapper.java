package com.foco.distributed.id.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foco.db.annotation.MultiIgnoreLogicDelete;
import com.foco.distributed.id.store.Segment;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/08/27 10:18
 */
@MultiIgnoreLogicDelete({"selectOne","updateById"})
public interface SegmentMapper extends BaseMapper<Segment> {
}
