package com.foco.distributed.id.renew;

import com.foco.context.core.DataSourceContextHolder;
import com.foco.distributed.id.generate.SegmentEntity;
import com.foco.distributed.id.store.AbstractSegmentService;
import com.foco.distributed.id.store.SegmentValue;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/11 16:03
 */
public abstract class AbstractSegmentRenew implements SegmentRenew{
    private AbstractSegmentService abstractSegmentService;
    public AbstractSegmentRenew(AbstractSegmentService abstractSegmentService) {
        this.abstractSegmentService = abstractSegmentService;
    }

    @Override
    public void renew(SegmentEntity segmentEntity) {
        SegmentValue segmentValue;
        segmentValue = DataSourceContextHolder.switchMasterTemporary(() ->
                abstractSegmentService.renewSegment(segmentEntity)
        );
        segmentEntity.setNextMax(segmentValue.getMax());
        segmentEntity.setNextCurrent(segmentValue.getCurrent());
        segmentEntity.setStep(segmentValue.getStep());
        segmentEntity.setNextFull(true);
    }

    @Override
    public void init(SegmentEntity segmentEntity) {
        //初始化当前值
        SegmentValue segmentValue = DataSourceContextHolder.switchMasterTemporary(() ->
                abstractSegmentService.initSegment(segmentEntity)
        );
        segmentEntity.setCurrent(segmentValue.getCurrent());
        segmentEntity.setMax(segmentValue.getMax());
        segmentEntity.setStep(segmentValue.getStep());
    }
}
