package com.foco.page.mvc.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.foco.context.common.FocoHttpMessage;
import com.foco.model.constant.FocoConstants;
import com.foco.model.constant.RequestBodyOrderConstants;
import com.foco.model.page.ThreadPagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @Description 分页json格式参数解析
 * @Author lucoo
 * @Date 2021/6/15 16:26
 **/
@RestControllerAdvice
@Slf4j
public class WebPageRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter implements Ordered {
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
            throws IOException {
        String httpBody = StreamUtils.copyToString(inputMessage.getBody(), Charset.defaultCharset());
        try {
            JSONObject jsonObject = JSON.parseObject(httpBody);
            ThreadPagingUtil.buildPageParam(jsonObject.getString(FocoConstants.PAGE_CURRENT),jsonObject.getString(FocoConstants.PAGE_SIZE));
        } catch (Exception e) {
        }
        return new FocoHttpMessage(new ByteArrayInputStream(httpBody.getBytes()), inputMessage.getHeaders());
    }

    @Override
    public int getOrder() {
        return RequestBodyOrderConstants.PAGING;
    }
}
