package com.foco.distributed.id.renew;

import com.foco.distributed.id.properties.IdGenerateProperties;
import com.foco.distributed.id.generate.SegmentEntity;

import java.text.DecimalFormat;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/11 13:48
 */
public class SegmentRenewSupport {
    IdGenerateProperties idGenerateProperties;
    public SegmentRenewSupport(IdGenerateProperties idGenerateProperties) {
        this.idGenerateProperties=idGenerateProperties;
    }

    public boolean shouldReNew(SegmentEntity segmentEntity) {
        Long current = segmentEntity.getCurrent().get();
        Integer stepValue = segmentEntity.getStep();
        long surplusValue = segmentEntity.getMax() - current;
        DecimalFormat df = new DecimalFormat("0.000000");
        Double num = Double.parseDouble(df.format((float) surplusValue / stepValue)) * 100;
        return num <= idGenerateProperties.getPercent();
    }

    public IdGenerateProperties getIdGenerateProperties() {
        return idGenerateProperties;
    }
}
