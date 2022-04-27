package com.foco.model.spi;

import com.foco.model.ApiResult;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/09 11:22
 */
public abstract class AbstractApiResultBuilder implements ApiResultBuilder {
    protected abstract void buildExtend(ApiResult result,Object... params);
    public ApiResult build(String code, String message,Object... params){
        ApiResult result = new ApiResult();
        result.setCode(code);
        result.setMsg(message);
        buildExtend(result,params);
        parsePlaceholder(result,params);
        TraceIdBuilderManager.buildTraceId(result);
        return result;
    }
    private void parsePlaceholder(ApiResult result,Object... params){
        String message = result.getMsg();
        if(!isEmpty(message)&&message.contains("%s")&& params!=null&& params.length!=0){
            String format;
            try {
                format = String.format(result.getMsg(), params);
            } catch (Exception e) {
                format=message;
            }
            result.setMsg(format);
        }
    }
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}
