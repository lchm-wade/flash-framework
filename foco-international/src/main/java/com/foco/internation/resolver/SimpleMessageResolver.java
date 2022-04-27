package com.foco.internation.resolver;

import cn.hutool.core.util.StrUtil;
import com.foco.internation.LocaleEntity;
import com.foco.internation.parser.PropertiesParser;
import com.foco.model.constant.FocoErrorCode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/13 15:52
 **/
public class SimpleMessageResolver implements MessageResolver {
    @Autowired
    private PropertiesParser propertiesParser;

    @Override
    public String resolveMessage(LocaleEntity localeEntity,String code, String message) {
        if (StrUtil.isEmpty(localeEntity.getLocale())) {
            return message;
        } else {
            if (FocoErrorCode.VALID_ERROR.getCode().equals(code)) {
                //参数校验框架返回的错误码
                StringBuilder builder = new StringBuilder();
                String[] codes = message.split(",");
                for (String singleCode : codes) {
                    Object singleValue = propertiesParser.getProperty(localeEntity, singleCode);
                    if (singleValue == null) {
                        singleValue = singleCode;
                    }
                    builder.append(singleValue).append("|");
                }
                String val = builder.toString();
                return val.substring(0, val.length() - 1);
            }
            String value = propertiesParser.getProperty(localeEntity, code);
            if (StrUtil.isEmpty(value)) {
                return message;
            }
            return value;
        }
    }
}
