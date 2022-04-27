package com.foco.distributed.id.renew;

import com.foco.distributed.id.generate.SegmentEntity;
import com.foco.distributed.id.generate.SegmentGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lucoo
 * @version 1.0.0
 * @description id续租定时器
 * @date 2021/08/27 11:43
 */
@Slf4j
public class SegmentReNewScheduler implements DisposableBean {
    Map<String, Thread> renewThreadContainer = new HashMap<>();
    Map<String, Boolean> renewFlagContainer = new HashMap<>();
    SegmentRenewSupport segmentRenewSupport;

    public SegmentReNewScheduler(SegmentRenewSupport segmentRenewSupport) {
        this.segmentRenewSupport = segmentRenewSupport;
    }

    public void start(SegmentGenerator segmentGenerator) {
        SegmentRenew segmentRenewManager = segmentGenerator.getIdRenewManager();
        SegmentEntity segmentEntity = segmentGenerator.getSegmentEntry();
        String bizTag=segmentEntity.getBizTag();
        renewFlagContainer.put(bizTag, false);
        Thread renewThread = new Thread(() -> {
            boolean stop = renewFlagContainer.get(bizTag);
            while (!stop) {
                try {
                    Thread.sleep(segmentRenewSupport.getIdGenerateProperties().getTimeBetweenEvictionRunsMillis());
                    //检测当前idGenerate使用比例
                    if (!segmentEntity.isNextFull() && segmentRenewSupport.shouldReNew(segmentEntity)) {
                        synchronized (segmentEntity) {
                            if (!segmentEntity.isNextFull() && segmentRenewSupport.shouldReNew(segmentEntity)) {
                                log.info("【定时续租】业务标签:{},当前值:{}达到最大范围值:{}的比值", bizTag, segmentEntity.getCurrent().get(), segmentEntity.getMax());
                                segmentRenewManager.renew(segmentEntity);
                                log.info("【定时续租】续租成功 业务标签:{}, nextCurrent:{} nextMax:{},", bizTag, segmentEntity.getNextCurrent().get(), segmentEntity.getNextMax());
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    log.warn("id reNew thread-{} is stopping",bizTag);
                } catch (Exception exception) {
                    log.error("id renew thread-{} exception", bizTag,exception);
                }
            }
            log.info("id reNew thread-{} is stoped",bizTag);
        });
        renewThread.setName("id renew thread-"+bizTag);
        renewThread.start();
        renewThreadContainer.put(bizTag, renewThread);
    }

    private void stop() {
        if (renewFlagContainer.size() != 0) {
            renewFlagContainer.values().forEach(t -> t = true);
        }
        if (renewThreadContainer.size() != 0) {
            renewThreadContainer.values().forEach(t ->
            {
                if (!t.isInterrupted()) {
                    t.interrupt();
                }
            });
        }
    }

    @Override
    public void destroy() throws Exception {
        stop();
    }
}
