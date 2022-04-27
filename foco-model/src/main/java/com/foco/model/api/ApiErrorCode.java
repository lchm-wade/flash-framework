package com.foco.model.api;

/**
 * description: 错误码规范定义
 *
 * @Author lucoo
 * @Date 2021/6/23 16:26
 */
public interface ApiErrorCode {
    /**
     * 获取错误码
     *
     * @return code
     */
    default String getCode() {
        return getProjectCode()+getModularCode();
    }
    String getProjectCode();
    String getModularCode();
    /**
     * 获取错误消息
     *
     * @return message
     */
    String getMsg();

    /**
     * 转换为消息
     *
     * @param code
     * @param message
     * @return string
     */
    default String toString(String code, String message) {
        return code + "：" + message;
    }
}
