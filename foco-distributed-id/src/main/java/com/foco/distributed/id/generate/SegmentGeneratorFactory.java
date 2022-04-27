package com.foco.distributed.id.generate;

import com.foco.distributed.id.properties.IdGenerateProperties;
import com.foco.distributed.id.generate.cycle.CycleSegmentEntity;
import com.foco.distributed.id.generate.cycle.CycleSegmentGenerator;
import com.foco.distributed.id.generate.cycle.SegmentData;
import com.foco.distributed.id.renew.CycleSegmentRenewManager;
import com.foco.distributed.id.renew.SegmentReNewScheduler;
import com.foco.distributed.id.renew.SegmentRenewManager;
import com.foco.distributed.id.tag.TagConfig;
import com.foco.distributed.id.tag.TagRegistration;
import com.foco.distributed.id.tag.TagRegistry;
import com.foco.model.constant.FocoErrorCode;
import com.foco.model.constant.TimePattern;
import com.foco.model.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author lucoo
 * @version 1.0.0
 * @description 号段管理
 * @date 2021/08/27 09:44
 */
@Slf4j
public class SegmentGeneratorFactory {
    private SegmentReNewScheduler segmentReNewScheduler;
    private Map<String, Generator> instances = new HashMap<>();
    private TagRegistration tagRegistration;
    private CycleSegmentRenewManager cycleSegmentRenewManager;
    private SegmentRenewManager segmentRenewManager;

    protected SegmentGeneratorFactory(CycleSegmentRenewManager cycleSegmentRenewManager, SegmentRenewManager segmentRenewManager, SegmentReNewScheduler segmentReNewScheduler, List<TagRegistry> tagRegisters, IdGenerateProperties idGenerateProperties) {
        this.segmentReNewScheduler = segmentReNewScheduler;
        this.cycleSegmentRenewManager = cycleSegmentRenewManager;
        this.segmentRenewManager = segmentRenewManager;
        tagRegistration = new TagRegistration();
        HashSet<TagConfig> tags = new HashSet<>();
        for (TagRegistry tagRegistry : tagRegisters) {
            tagRegistry.register(tags);
        }
        tags.stream().forEach(it -> tagRegistration.register(it));
        if (idGenerateProperties.isInit()) {
            for (TagConfig tagConfig : tags) {
                String bizTag = tagConfig.getBizTag();
                TimePattern timePattern = tagConfig.getTimePattern();
                if (timePattern != null) {
                    initCycle(bizTag, timePattern.getValue());
                } else {
                    init(bizTag);
                }
            }
        }
    }


    protected long generateNo(String bizTag) {
        TagConfig tagConfig = tagRegistration.getTagConfig(bizTag);
        if (tagConfig == null) {
            SystemException.throwException(FocoErrorCode.PARAMS_VALID.getCode(), String.format("bizTag:{%s} not exist", bizTag));
        }
        if (tagConfig.getTimePattern() != null) {
            SystemException.throwException(FocoErrorCode.PARAMS_VALID.getCode(), String.format("bizTag:{%s} and Generator type no match", bizTag));
        }
        if (instances.get(bizTag) == null) {
            synchronized (bizTag.intern()) {
                if (instances.get(bizTag) == null) {
                    init(bizTag);
                }
            }
        }
        return instances.get(bizTag).generateNo();
    }

    protected SegmentData generateByTime(String bizTag) {
        TagConfig tagConfig = tagRegistration.getTagConfig(bizTag);
        if (tagConfig == null) {
            SystemException.throwException(FocoErrorCode.PARAMS_VALID.getCode(), String.format("bizTag:{%s} not exist", bizTag));
        }
        if (tagConfig.getTimePattern() == null) {
            SystemException.throwException(FocoErrorCode.PARAMS_VALID.getCode(), String.format("bizTag:{%s} and Generator type no match", bizTag));
        }
        if (instances.get(bizTag) == null) {
            synchronized (bizTag.intern()) {
                if (instances.get(bizTag) == null) {
                    initCycle(bizTag, tagConfig.getTimePattern().getValue());
                }
            }
        }
        return instances.get(bizTag).generateByTime();
    }

    private void init(String bizTag) {
        //初始化当前值
        SegmentEntity entity = new SegmentEntity(bizTag);
        segmentRenewManager.init(entity);
        startRenew(new SegmentGenerator(entity, segmentRenewManager));
    }

    private void initCycle(String bizTag, String timePattern) {
        CycleSegmentEntity cycleSegmentEntity = new CycleSegmentEntity(bizTag, timePattern);

        CycleSegmentGenerator cycleSegmentGenerator = new CycleSegmentGenerator(cycleSegmentEntity, cycleSegmentRenewManager);
        //初始化当前值
        cycleSegmentRenewManager.init(cycleSegmentEntity);
        startRenew(cycleSegmentGenerator);
        cycleSegmentGenerator.init();
    }

    private void startRenew(SegmentGenerator segmentGenerator) {
        //开启续租线程
        segmentReNewScheduler.start(segmentGenerator);
        instances.put(segmentGenerator.getSegmentEntry().getBizTag(), segmentGenerator);
    }
}
