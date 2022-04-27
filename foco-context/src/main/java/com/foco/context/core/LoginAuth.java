package com.foco.context.core;
/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/9 9:46
 *
 **/
public interface LoginAuth {
    /**
     * @param token:请求的token值
     * @param clientType:请求头的客户端类型值
     * @param networkType:请求头的网络类型值
     * @return 若用户登录态检验成功则返回LoginContext,否则返回null
     */
    LoginContext auth(String token,String clientType,String networkType);
}
