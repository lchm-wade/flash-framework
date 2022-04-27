package com.foco.distributed.id.renew;

import com.foco.distributed.id.generate.cycle.CycleSegmentEntity;
import com.foco.distributed.id.store.CycleSegmentService;
import com.foco.distributed.id.store.SegmentValue;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lucoo
 * @version 1.0.0
 * @description 号段管理
 * @date 2021/08/27 09:44
 */
@Slf4j
public class CycleSegmentRenewManager extends AbstractSegmentRenew implements CycleSegmentRenew {
    private CycleSegmentService cycleSegmentService;

    public CycleSegmentRenewManager(CycleSegmentService cycleSegmentService) {
        super(cycleSegmentService);
        this.cycleSegmentService = cycleSegmentService;
    }
    @Override
    public void reset(CycleSegmentEntity segmentEntity, String time) {
        //重置
        SegmentValue segmentValue = cycleSegmentService.resetSegment(segmentEntity.getBizTag(), time);
        segmentEntity.setCurrent(segmentValue.getCurrent());
        segmentEntity.setMax(segmentValue.getMax());
        segmentEntity.setStep(segmentValue.getStep());

        segmentEntity.setNextMax(0L);
        segmentEntity.setNextCurrent(0L);
        segmentEntity.setNextFull(false);

        segmentEntity.setNextIsNewTime(false);
        segmentEntity.setCurrIsNewTime(false);
    }
}
