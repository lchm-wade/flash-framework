package com.foco.distributed.id.generate;

import com.foco.distributed.id.renew.SegmentRenew;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lucoo
 * @version 1.0.0
 * @description id生成器
 * 双buffer设计
 * @date 2021/08/27 09:42
 */
@Slf4j
public class SegmentGenerator implements Generator {
    protected SegmentRenew segmentRenewManager;
    protected SegmentEntity segmentEntity;

    protected SegmentGenerator(SegmentEntity segmentEntity, SegmentRenew segmentRenewManager) {
        this.segmentRenewManager = segmentRenewManager;
        this.segmentEntity = segmentEntity;
    }

    @Override
    public synchronized long generateNo() {
        long curValue = segmentEntity.getCurrent().incrementAndGet();
        if (curValue > segmentEntity.getMax()) {
            if (segmentEntity.isNextFull()) {
                //下一个buff已经充满,直接切换号段
                refresh();
            } else {
                //防止续租线程忙没有及时续租,这里手动续租
                synchronized (segmentEntity) {
                    if (!segmentEntity.isNextFull()) {
                        log.info("【手动续租】业务标签:{}", segmentEntity.getBizTag());
                        segmentRenewManager.renew(segmentEntity);
                        log.info("【手动续租】续租成功 业务标签:{},nextCurrent:{}，nextMax:{},", segmentEntity.getBizTag(), segmentEntity.getNextCurrent().get(), segmentEntity.getNextMax());
                    }
                }
            }
            return generateNo();
        }
        return curValue;
    }

    protected void refresh() {
        log.info("【号段切换】业务标签:{} 号段消耗完毕,切换下个号段", segmentEntity.getBizTag());
        segmentEntity.getCurrent().set(segmentEntity.getNextCurrent().get());
        segmentEntity.setMax(segmentEntity.getNextMax());
        segmentEntity.setNextFull(false);
    }

    public SegmentRenew getIdRenewManager() {
        return segmentRenewManager;
    }

    public SegmentEntity getSegmentEntry() {
        return segmentEntity;
    }
}
