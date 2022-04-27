package com.foco.distributed.id.store;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/08/27 11:17
 */
@Getter
@Setter
public class SegmentValue {
    private Long max;
    private Long current;
    private Integer step;
}
