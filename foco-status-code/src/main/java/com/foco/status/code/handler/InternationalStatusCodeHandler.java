package com.foco.status.code.handler;

import cn.hutool.core.util.StrUtil;
import com.foco.internation.LocaleEntity;
import com.foco.properties.SystemConfig;
import com.foco.context.common.StatusCode;
import com.foco.context.core.SpringContextHolder;
import com.foco.context.util.HttpContext;
import com.foco.internation.resolver.MessageResolver;
import com.foco.internation.resolver.SimpleMessageResolver;
import com.foco.model.constant.FocoConstants;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-07-06 17:06
 */
public class InternationalStatusCodeHandler implements IStatusCodeHandler{
    private static final String split="-";
    private static MessageResolver messageResolver= null;
    public InternationalStatusCodeHandler(SpringContextHolder springContextHolder){
        messageResolver= springContextHolder.getBean(SimpleMessageResolver.class);
    }
    public  String resolveConvert(String simpleName, String value, StatusCode[] statusCodes){
        String locale = HttpContext.getHeader(SystemConfig.getConfig().getLocaleHead());
        Optional<String> optional = Arrays.stream(statusCodes).
                filter(statusCode -> statusCode.getCode().equals(value)).
                map((statusCode) -> messageResolver.resolveMessage(new LocaleEntity().setLocale(locale).setType(FocoConstants.STATUS_CODE_FILE+simpleName+split), statusCode.getCode(), statusCode.getMessage())).
                findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return value;
    }
    public  String resolveFormat(String simpleName, String value, StatusCode[] statusCodes){
        String locale = HttpContext.getHeader(SystemConfig.getConfig().getLocaleHead());
        if (StrUtil.isNotBlank(locale)) {
            return messageResolver.resolveMessage(new LocaleEntity().setLocale(locale).setType(FocoConstants.STATUS_CODE_REVERS_FILE+simpleName+split), value,null);
        }
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
