package com.foco.context.core;

import com.foco.model.constant.FocoConstants;

public interface IContext {
    String get();

    String get(Boolean direct);

    void set(String context);

    void remove();
    default String buildKey(IContext iContext){
        return FocoConstants.FOCO_CONTEXT+"."+ iContext.getClass().getName();
    }
}
