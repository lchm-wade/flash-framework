package com.foco.context.core;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description
 * @Author
 * @Date 2021/6/1 18:18
 **/
@Setter
@Getter
public class GenericLoginContext extends LoginContext {
    /**
     * 客户端类型
     */
    private String clientType;
    /**
     * 网络类型
     */
    private String networkType;
    /**
     * token 有效期 以秒为单位
     */
    private Long expireTimeInSecond;

}
