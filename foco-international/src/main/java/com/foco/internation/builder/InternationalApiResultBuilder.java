package com.foco.internation.builder;
import cn.hutool.core.util.StrUtil;
import com.foco.context.core.SpringContextHolder;
import com.foco.internation.LocaleEntity;
import com.foco.internation.resolver.MessageResolver;
import com.foco.internation.resolver.SimpleMessageResolver;
import com.foco.properties.SystemConfig;
import com.foco.context.util.HttpContext;
import com.foco.model.ApiResult;
import com.foco.model.constant.FocoConstants;
import com.foco.model.spi.AbstractApiResultBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-06-25 14:58
 */
@Slf4j
public class InternationalApiResultBuilder extends AbstractApiResultBuilder {
    private static String localeHead=SystemConfig.getConfig().getLocaleHead();
    protected MessageResolver messageResolver=SpringContextHolder.getBean(SimpleMessageResolver.class);
    @Override
    public void buildExtend(ApiResult result,Object... params) {
        String locale = HttpContext.getHeader(localeHead);
        if (StrUtil.isNotEmpty(locale)) {
            result.setMsg(messageResolver.resolveMessage(new LocaleEntity().setLocale(locale).setType(FocoConstants.ERROR_CODE_FILE), result.getCode(), result.getMsg()));
        }
    }
}
