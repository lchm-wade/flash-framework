package com.foco.distributed.id.generate.cycle;

import com.foco.distributed.id.generate.SegmentEntity;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/02 13:41
 */
@Slf4j
public class CycleSegmentEntity extends SegmentEntity {
    //当前时间,服务器各个节点时差校准
    private volatile String currentTime="";
    //下个buffer数据是否是新的时间单位的
    private volatile boolean nextIsNewTime=false;
    //当前buffer数据是否是新的时间单位的
    private volatile boolean currIsNewTime=false;
    private String timePattern;

    public CycleSegmentEntity(String bizTag,String timePattern) {
        super(bizTag);
        this.timePattern=timePattern;
        currentTime=LocalDateTime.now().format(DateTimeFormatter.ofPattern(timePattern));
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public boolean isNextIsNewTime() {
        return nextIsNewTime;
    }

    public void setNextIsNewTime(boolean nextIsNewTime) {
        this.nextIsNewTime = nextIsNewTime;
    }

    public boolean isCurrIsNewTime() {
        return currIsNewTime;
    }

    public void setCurrIsNewTime(boolean currIsNewTime) {
        this.currIsNewTime = currIsNewTime;
    }

    public String getTimePattern() {
        return timePattern;
    }
}
