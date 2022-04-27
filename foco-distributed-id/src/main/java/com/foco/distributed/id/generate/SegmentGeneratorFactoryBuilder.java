package com.foco.distributed.id.generate;

import com.foco.distributed.id.properties.IdGenerateProperties;
import com.foco.distributed.id.renew.CycleSegmentRenewManager;
import com.foco.distributed.id.renew.SegmentReNewScheduler;
import com.foco.distributed.id.renew.SegmentRenewManager;
import com.foco.distributed.id.tag.TagRegistry;

import java.util.List;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/24 18:13
 */
public class SegmentGeneratorFactoryBuilder {
    private SegmentReNewScheduler segmentReNewScheduler;
    private CycleSegmentRenewManager cycleSegmentRenewManager;
    private SegmentRenewManager segmentRenewManager;
    private List<TagRegistry> tagRegisters;
    private IdGenerateProperties idGenerateProperties;

    public SegmentGeneratorFactory build() {
        return new SegmentGeneratorFactory(cycleSegmentRenewManager, segmentRenewManager, segmentReNewScheduler, tagRegisters, idGenerateProperties);
    }

    public SegmentGeneratorFactoryBuilder setSegmentReNewScheduler(SegmentReNewScheduler segmentReNewScheduler) {
        this.segmentReNewScheduler = segmentReNewScheduler;
        return this;
    }

    public SegmentGeneratorFactoryBuilder setCycleSegmentRenewManager(CycleSegmentRenewManager cycleSegmentRenewManager) {
        this.cycleSegmentRenewManager = cycleSegmentRenewManager;
        return this;
    }

    public SegmentGeneratorFactoryBuilder setSegmentRenewManager(SegmentRenewManager segmentRenewManager) {
        this.segmentRenewManager = segmentRenewManager;
        return this;
    }

    public SegmentGeneratorFactoryBuilder setTagRegisters(List<TagRegistry> tagRegisters) {
        this.tagRegisters = tagRegisters;
        return this;
    }

    public SegmentGeneratorFactoryBuilder setIdGenerateProperties(IdGenerateProperties idGenerateProperties) {
        this.idGenerateProperties = idGenerateProperties;
        return this;
    }
}
