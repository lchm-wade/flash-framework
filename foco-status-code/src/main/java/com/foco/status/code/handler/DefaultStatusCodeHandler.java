package com.foco.status.code.handler;

import com.foco.context.common.StatusCode;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-07-06 17:06
 */
public class DefaultStatusCodeHandler implements IStatusCodeHandler{
    public  String resolveConvert(String simpleName, String value, StatusCode[] statusCodes){
        Optional<String> optional = Arrays.stream(statusCodes).
                filter(statusCode -> statusCode.getCode().equals(value)).
                map((statusCode) -> statusCode.getMessage()).
                findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return value;
    }
    public  String resolveFormat(String simpleName, String value, StatusCode[] statusCodes){
        Optional<String> optional = Arrays.stream(statusCodes).
                filter(statusCode -> statusCode.getMessage().equals(value)).
                map((statusCode) -> statusCode.getCode()).
                findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return value;
    }
}
