package com.foco.model.constant;

import com.foco.model.api.ApiErrorCode;
import com.foco.model.spi.SystemCodeManager;

/**
 * description： 常用错误定义
 *
 * @Author lucoo
 * @Date 2021/6/23 16:26
 */
public enum FocoErrorCode implements ApiErrorCode {
    /***状态码*/
    SUCCESS(SystemCodeManager.getSuccessCode(), "请求成功"),
    GATE_WAY_ERROR("40000", "网关异常"),
    PARAMS_VALID("40001", "参数不合法"),
    CONFIG_VALID("40002", "配置不合法"),
    PARAMS_ERROR("50000", "参数校验错误"),
    UNAUTHORIZED("50001", "身份认证未通过"),
    SYSTEM_ERROR("50002", "系统异常"),

    LIMIT_RATE_ERROR("50003", "当前用户访问频繁,请稍后再试"),
    PATH_ERROR("50004", "请求路径错误"),
    VALID_ERROR("50005", "校验框架错误"),
    EXTERNAL_ERROR("50006", "调用外部系统异常"),
    DECRYPT_ERROR("60001", "解密异常"),
    ENCRYPT_ERROR("60002", "加密异常"),
    CHECK_SIGN_ERROR("60003", "签名不合法"),
    TENANT_ID_NOT_EXIT("60004", "租户id不存在"),
    HASH_TARGET_NOT_EXIT("60005", "顺序消息下hashTarget不能为空"),
    TOPIC_NOT_EXIT("60006", "Topic不能为空"),

    SENTINEL_FLOW_ERROR("99999", "系统繁忙,请稍后访问"),
    ;
    private String code;
    private String msg;

    FocoErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getProjectCode() {
        return SystemCodeManager.getSystemCodePrefix();
    }

    @Override
    public String getModularCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return toString(getCode(), getMsg());
    }
}
