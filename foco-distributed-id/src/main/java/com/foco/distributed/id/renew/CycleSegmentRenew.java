package com.foco.distributed.id.renew;

import com.foco.distributed.id.generate.cycle.CycleSegmentEntity;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/11 13:45
 */
public interface CycleSegmentRenew{
    void reset(CycleSegmentEntity segmentEntity, String time);
}
