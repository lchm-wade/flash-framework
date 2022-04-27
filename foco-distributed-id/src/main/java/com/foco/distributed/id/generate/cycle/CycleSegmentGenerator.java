package com.foco.distributed.id.generate.cycle;

import com.foco.distributed.id.generate.SegmentGenerator;
import com.foco.distributed.id.renew.CycleSegmentRenew;
import com.foco.distributed.id.renew.SegmentRenew;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lucoo
 * @version 1.0.0
 * @description 带时间周期的id生成器
 *继承SegmentGenerator
 * 完全复用SegmentGenerator 预加载+双buffer+本地内存递增 特性
 * 在时间周期进入下个阶段时，重置号段
 * @date 2021/08/28 19:31
 */
@Slf4j
public class CycleSegmentGenerator extends SegmentGenerator {
    private String timePattern;
    Map<String, CycleSegmentGenerator> cycleSegmentEntityMap=new HashMap<>();
    private CycleSegmentRenew cycleSegmentRenew;
    public CycleSegmentGenerator(CycleSegmentEntity segmentEntity, SegmentRenew segmentRenewManager) {
        super(segmentEntity,segmentRenewManager);
        this.timePattern=segmentEntity.getTimePattern();
        this.cycleSegmentRenew= (CycleSegmentRenew) segmentRenewManager;
    }
    @Override
    public SegmentData generateByTime() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(timePattern));
        checkSegment(time);
        long result = super.generateNo();
        time=checkTime(time);
        return new SegmentData(time,result);
    }
    protected void refresh() {
        super.refresh();
        CycleSegmentEntity cycleSegmentEntity = (CycleSegmentEntity) this.segmentEntity;

        if(cycleSegmentEntity.isCurrIsNewTime()){
            cycleSegmentEntity.setCurrIsNewTime(false);
        }
        if(cycleSegmentEntity.isNextIsNewTime()){
            cycleSegmentEntity.setCurrIsNewTime(true);
            cycleSegmentEntity.setNextIsNewTime(false);
        }
    }
    private void checkSegment(String time){
        if(cycleSegmentEntityMap.get(time)==null){
            //当前时间段已过,获取下个时间段
            synchronized (segmentEntity){
                if(cycleSegmentEntityMap.get(time)==null){
                    cycleSegmentRenew.reset((CycleSegmentEntity)segmentEntity,time);
                    cycleSegmentEntityMap.clear();
                    cycleSegmentEntityMap.put(time,this);
                }
            }
        }
    }
    public void init(){
        String localTime=LocalDateTime.now().format(DateTimeFormatter.ofPattern(timePattern));
        cycleSegmentEntityMap.put(localTime,this);
    }
    private String checkTime(String localTime){
        CycleSegmentEntity cycleSegmentEntity = (CycleSegmentEntity) this.segmentEntity;
        String currentTime =cycleSegmentEntity.getCurrentTime();
        if(localTime.compareTo(currentTime)<0&&cycleSegmentEntity.isCurrIsNewTime()){
            localTime=currentTime;
        }
        return localTime;
    }
}
