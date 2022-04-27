package com.foco.distributed.id.store;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.foco.distributed.id.generate.SegmentEntity;
import com.foco.distributed.id.store.mapper.SegmentMapper;
import com.foco.model.constant.FocoErrorCode;
import com.foco.model.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/08 09:08
 */
public abstract class AbstractSegmentService{
    @Autowired
    protected SegmentMapper segmentMapper;
    protected abstract void doInitSegment(SegmentEntity segmentEntity, Segment segment);
    protected abstract void doRenewSegment(SegmentEntity segmentEntity, Segment segment);
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public SegmentValue initSegment(SegmentEntity segmentEntity) {
        Segment segment = check(segmentEntity.getBizTag());
        doInitSegment(segmentEntity,segment);
        return buildValue(segment);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public SegmentValue renewSegment(SegmentEntity segmentEntity) {
        Segment segment = check(segmentEntity.getBizTag());
        doRenewSegment(segmentEntity,segment);
        return buildValue(segment);
    }
    protected SegmentValue buildValue(Segment segment) {
        SegmentValue segmentValue = new SegmentValue();
        segmentValue.setCurrent(segment.getCurrentVal());
        segmentValue.setMax(segment.getCurrentVal() + segment.getStep());
        segmentValue.setStep(segment.getStep());
        //更新号段区间
        segment.setCurrentVal(segment.getCurrentVal() + segment.getStep());
        segmentMapper.updateById(segment);
        return segmentValue;
    }
    protected Segment check(String bizTag) {
        LambdaQueryWrapper<Segment> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Segment::getBizTag, bizTag)
                .select(Segment::getStep, Segment::getCurrentVal, Segment::getId, Segment::getInitVal, Segment::getResetTime)
                .last("limit 1 for update");
        Segment segment = segmentMapper.selectOne(queryWrapper);
        if (segment == null) {
            SystemException.throwException(FocoErrorCode.SYSTEM_ERROR.getCode(), String.format("bizTag:{%s} no configuration information", bizTag));
        }
        if(segment.getInitVal()==null){
            SystemException.throwException(FocoErrorCode.CONFIG_VALID.getCode(), String.format("bizTag:{%s} initVal cannot be null", bizTag));
        }
        if(segment.getCurrentVal()==null){
            SystemException.throwException(FocoErrorCode.CONFIG_VALID.getCode(), String.format("bizTag:{%s} currentVal cannot be null", bizTag));
        }
        if(segment.getStep()==null){
            SystemException.throwException(FocoErrorCode.CONFIG_VALID.getCode(), String.format("bizTag:{%s} step cannot be null", bizTag));
        }
        return segment;
    }
}
