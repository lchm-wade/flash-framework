package com.foco.distributed.id.generate;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/02 13:41
 */
@Slf4j
public class SegmentEntity {
    protected String bizTag;
    protected volatile Long max = 0L;
    protected AtomicLong current = new AtomicLong();
    protected Integer step = 0;
    protected Long nextMax = 0L;
    protected AtomicLong nextCurrent = new AtomicLong();
    protected volatile boolean nextFull = false;
    public void setMax(Long max) {
        this.max = max;
    }

    public void setCurrent(Long currentValue) {
        current.set(currentValue);
    }

    public AtomicLong getCurrent() {
        return current;
    }

    public Long getMax() {
        return max;
    }

    public Long getNextMax() {
        return nextMax;
    }

    public void setNextMax(Long nextMax) {
        this.nextMax = nextMax;
    }

    public AtomicLong getNextCurrent() {
        return nextCurrent;
    }

    public void setNextCurrent(Long nextCurrentValue) {
        nextCurrent.set(nextCurrentValue);
    }
    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }
    public boolean isNextFull() {
        return nextFull;
    }
    public void setNextFull(boolean nextFull) {
        this.nextFull = nextFull;
    }

    public SegmentEntity(String bizTag) {
        this.bizTag = bizTag;
    }
    public String getBizTag() {
        return bizTag;
    }
}
