package com.foco.status.code.handler;

import com.foco.context.common.StatusCode;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/09 10:47
 */
public interface IStatusCodeHandler {
    String resolveConvert(String simpleName, String value, StatusCode[] statusCodes);

    String resolveFormat(String simpleName, String value, StatusCode[] statusCodes);
}
