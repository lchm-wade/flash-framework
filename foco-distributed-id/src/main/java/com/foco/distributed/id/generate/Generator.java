package com.foco.distributed.id.generate;

import com.foco.distributed.id.generate.cycle.SegmentData;

public interface Generator {
     default long generateNo(){
          return 0L;
     }
     default SegmentData generateByTime(){
          return null;
     }
}
