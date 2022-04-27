package com.foco.distributed.id.generate.cycle;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/06 15:27
 */
public class SegmentData {
    private String time;
    private Long result;

    public SegmentData(String time, Long result) {
        this.time = time;
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public Long getResult() {
        return result;
    }
}
