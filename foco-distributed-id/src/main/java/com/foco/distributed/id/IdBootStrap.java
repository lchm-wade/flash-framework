package com.foco.distributed.id;

import com.foco.context.util.BootStrapPrinter;
import com.foco.distributed.id.properties.IdGenerateProperties;
import com.foco.distributed.id.generate.SegmentGeneratorFactory;
import com.foco.distributed.id.generate.SegmentGeneratorFactoryBuilder;
import com.foco.distributed.id.renew.CycleSegmentRenewManager;
import com.foco.distributed.id.renew.SegmentReNewScheduler;
import com.foco.distributed.id.renew.SegmentRenewManager;
import com.foco.distributed.id.renew.SegmentRenewSupport;
import com.foco.distributed.id.store.CycleSegmentService;
import com.foco.distributed.id.store.SegmentService;
import com.foco.distributed.id.tag.TagRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/08/27 09:32
 */
@Slf4j
@EnableConfigurationProperties(IdGenerateProperties.class)
public class IdBootStrap {
    @PostConstruct
    public void init() {
        BootStrapPrinter.log("foco-distributed-id",this.getClass());
    }
    @Bean
    SegmentService segmentService() {
        return new SegmentService();
    }

    @Bean
    CycleSegmentService cycleSegmentService() {
        return new CycleSegmentService();
    }

    @Bean
    SegmentRenewManager idRenewManager(SegmentService segmentService) {
        return new SegmentRenewManager(segmentService);
    }

    @Bean
    CycleSegmentRenewManager cycleSegmentRenewManager(CycleSegmentService cycleSegmentService) {
        return new CycleSegmentRenewManager(cycleSegmentService);
    }

    @Bean
    SegmentRenewSupport segmentRenewSupport(IdGenerateProperties idGenerateProperties) {
        return new SegmentRenewSupport(idGenerateProperties);
    }

    @Bean
    SegmentReNewScheduler idReNewScheduler(SegmentRenewSupport segmentRenewSupport) {
        return new SegmentReNewScheduler(segmentRenewSupport);
    }

    @Bean
    SegmentGeneratorFactory segmentManager(CycleSegmentRenewManager cycleSegmentRenewManager, SegmentRenewManager segmentRenewManager, SegmentReNewScheduler segmentReNewScheduler, List<TagRegistry> tagRegisters, IdGenerateProperties idGenerateProperties) {
        return new SegmentGeneratorFactoryBuilder()
                .setCycleSegmentRenewManager(cycleSegmentRenewManager)
                .setSegmentRenewManager(segmentRenewManager)
                .setSegmentReNewScheduler(segmentReNewScheduler)
                .setTagRegisters(tagRegisters)
                .setIdGenerateProperties(idGenerateProperties).build();
    }

}
