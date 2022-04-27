package com.foco.distributed.id.tag;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/09/18 10:07
 */
public class TagRegistration {
    private Map<String,TagConfig> registration=new HashMap<>();
    public void register(TagConfig tagConfig){
        registration.put(tagConfig.getBizTag(),tagConfig);
    }
    public TagConfig getTagConfig(String tag){
        return registration.get(tag);
    }
}
