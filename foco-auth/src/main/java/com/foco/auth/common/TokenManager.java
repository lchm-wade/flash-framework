package com.foco.auth.common;

import com.foco.context.core.GenericLoginContext;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/3 9:29
 **/
public interface TokenManager {
    String createToken(GenericLoginContext loginContext);
    boolean updateToken(GenericLoginContext loginContext);
    boolean removeToken();
}
