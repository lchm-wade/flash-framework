package com.foco.distributed.id.tag;

import java.util.Set;

/**
 * 业务标签实现该接口自动注册
 */
public interface TagRegistry {
     void register(Set<TagConfig> tags);
}
