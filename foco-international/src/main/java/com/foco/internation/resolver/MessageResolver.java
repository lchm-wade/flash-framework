package com.foco.internation.resolver;

import com.foco.internation.LocaleEntity;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/13 15:52
 **/
public interface MessageResolver {
    String resolveMessage(LocaleEntity localeEntity,String code, String message);
}
