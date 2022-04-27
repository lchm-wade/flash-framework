package com.foco.distributed.id.tag;

import com.foco.model.constant.TimePattern;

import java.util.Objects;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/11 12:36
 */
public class TagConfig {
    private String bizTag;
    private TimePattern timePattern;

    public TagConfig(String bizTag, TimePattern timePattern) {
        this.bizTag = bizTag;
        this.timePattern = timePattern;
    }

    public TagConfig(String bizTag) {
        this.bizTag = bizTag;
    }

    public String getBizTag() {
        return bizTag;
    }

    public TimePattern getTimePattern() {
        return timePattern;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagConfig config = (TagConfig) o;
        return Objects.equals(bizTag, config.bizTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bizTag);
    }
}
