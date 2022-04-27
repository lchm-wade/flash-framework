package com.foco.context.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * description： 响应处理工具
 *
 * @Author lucoo
 * @Date 2021/6/24 11:16
 */
@Slf4j
public class ResponseUtils {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
    public static void write(int status, String message){
        HttpServletResponse response = HttpContext.getResponse();
        if (response == null) {
            log.warn("response is null.");
            return;
        }
        if (response.isCommitted()) {
            log.error("can not write message,the stream has closed.");
            return;
        }
        response.setStatus(status);
        response.setHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE);
        try (Writer writer = response.getWriter()) {
            if (writer != null) {
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                writer.write(XXSFilterUtil.filerSpecialChar(message));
            }
        } catch (IOException e) {
            log.error("writeErrorMessage error.", e);
        }
    }
    public static void write(int status, Throwable cause) {
        write(status, JSON.toJSONString(cause));
    }
}
