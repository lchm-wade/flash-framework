package com.foco.distributed.id.store;

import cn.hutool.core.util.StrUtil;
import com.foco.distributed.id.generate.SegmentEntity;
import com.foco.distributed.id.generate.cycle.CycleSegmentEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/08/27 10:18
 */
public class CycleSegmentService extends AbstractSegmentService{
    @Override
    protected void doInitSegment(SegmentEntity segmentEntity,Segment segment) {
            CycleSegmentEntity cycleSegmentEntity = (CycleSegmentEntity) segmentEntity;
            if (StrUtil.isBlank(segment.getResetTime()) && StrUtil.isNotBlank(cycleSegmentEntity.getCurrentTime())) {
                //初始化ResetTime
                segment.setResetTime(cycleSegmentEntity.getCurrentTime());
            }
            if (StrUtil.isNotBlank(segment.getResetTime()) && segment.getResetTime().compareTo(cycleSegmentEntity.getCurrentTime()) < 0) {
                //取集群中时钟最大的节点的值
                segment.setResetTime(cycleSegmentEntity.getCurrentTime());
            }
        }
    @Override
    protected void doRenewSegment(SegmentEntity segmentEntity,Segment segment) {
        CycleSegmentEntity cycleSegmentEntity=(CycleSegmentEntity) segmentEntity;
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(cycleSegmentEntity.getTimePattern()));
        if (StrUtil.isNotBlank(segment.getResetTime())
                && segment.getResetTime().compareTo(time) > 0) {
            //校准时钟
            cycleSegmentEntity.setCurrentTime(segment.getResetTime());
            cycleSegmentEntity.setNextIsNewTime(true);
        }
    }
    /**
     * 带时间周期的号段重置初始值
     *
     * @param bizTag
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public SegmentValue resetSegment(String bizTag, String currentTime) {
        Segment segment = check(bizTag);
        if (!currentTime.equals(segment.getResetTime())) {
            //重置
            segment.setCurrentVal(segment.getInitVal());
            segment.setResetTime(currentTime);

            Segment update = new Segment();
            update.setCurrentVal(segment.getCurrentVal());
            update.setId(segment.getId());
            update.setResetTime(currentTime);
            segmentMapper.updateById(update);
        }
        //构造value
        SegmentValue segmentValue = buildValue(segment);
        return segmentValue;
    }
}
