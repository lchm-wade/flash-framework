package com.foco.distributed.id.renew;

import com.foco.distributed.id.generate.SegmentEntity;

public interface SegmentRenew {
    void renew(SegmentEntity segmentEntity);
    void init(SegmentEntity segmentEntity);
}
