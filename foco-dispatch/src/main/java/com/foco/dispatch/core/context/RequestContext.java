package com.foco.dispatch.core.context;

import com.foco.dispatch.core.handler.PretaRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/11 18:10
 **/
@Setter
@Getter
public class RequestContext {
    private ClassMethod classMethod;
    private PretaRequest pretaRequest;

    public RequestContext(ClassMethod classMethod, PretaRequest pretaRequest) {
        this.classMethod = classMethod;
        this.pretaRequest = pretaRequest;
    }
}
