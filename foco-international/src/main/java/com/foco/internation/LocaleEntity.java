package com.foco.internation;

import lombok.Getter;
/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/10/13 09:45
 */
@Getter
public class LocaleEntity {
    /**
     * 项目id
     */
    private String appId="default";
    /**
     * 类型
     * 状态码或者错误码
     */
    private String type;
    /**
     * 语言
     */
    private String locale;

    public LocaleEntity setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public LocaleEntity setType(String type) {
        this.type = type;
        return this;
    }

    public LocaleEntity setLocale(String locale) {
        this.locale = locale;
        return this;
    }
}
