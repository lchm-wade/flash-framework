package com.foco.distributed.id.renew;

import com.foco.distributed.id.store.SegmentService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lucoo
 * @version 1.0.0
 * @description 号段管理
 * @date 2021/08/27 09:44
 */
@Slf4j
public class SegmentRenewManager extends AbstractSegmentRenew {
    public SegmentRenewManager(SegmentService segmentService) {
        super(segmentService);
    }
}
