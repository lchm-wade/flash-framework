package com.foco.crypt.inteceptor.web;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foco.properties.SystemConfig;
import com.foco.context.annotation.ConditionalOnMissingCloud;
import com.foco.context.util.HttpContext;
import com.foco.crypt.CryptHandler;
import com.foco.crypt.SignHandler;
import com.foco.crypt.properties.CryptProperties;
import com.foco.crypt.properties.SignProperties;
import com.foco.crypt.core.CryptRequest;
import com.foco.crypt.core.SignRequest;
import com.foco.model.ApiResult;
import com.foco.model.constant.FocoErrorCode;
import com.foco.model.constant.ResponseBodyOrderConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Description 加密
 * @Author lucoo
 * @Date 2021/6/15 16:26
 **/
@RestControllerAdvice
@ConditionalOnMissingCloud
public class EnCryptResponseBodyAdviceAdapter implements ResponseBodyAdvice, Ordered {
    @Autowired
    private CryptHandler cryptHandler;
    @Autowired
    private SignHandler signHandler;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        CryptRequest cryptRequest = new CryptRequest();
        cryptRequest.setAppId(HttpContext.getHeader(SystemConfig.getConfig().getAppId()));
        cryptRequest.setAlgorithm(HttpContext.getHeader(CryptProperties.getConfig().getCryptAlgorithmHead()));
        cryptRequest.setUrl(HttpContext.getRequest().getServletPath());
        Object source = body;
        Object o;
        try {
            String content;
            if (source instanceof String) {
                content = (String) source;
            } else {
                content = objectMapper.writeValueAsString(source);
            }
            SignRequest signRequest = new SignRequest();
            signRequest.setAppId(HttpContext.getHeader(SystemConfig.getConfig().getAppId()));
            signRequest.setAlgorithm(HttpContext.getHeader(SignProperties.getConfig().getSignAlgorithmHead()));
            signRequest.setUrl(HttpContext.getRequest().getServletPath());
            signRequest.setMethod(HttpContext.getRequest().getMethod());
            String sign = signHandler.sign(content, signRequest);
            if (StrUtil.isNotBlank(sign) && !content.equals(sign)) {
                response.getHeaders().add(SignProperties.getConfig().getSignHead(), sign);
            }
            o = cryptHandler.enCrypt(body, cryptRequest);
        } catch (Exception e) {
            o = ApiResult.error(FocoErrorCode.SYSTEM_ERROR.getCode(), e.getMessage());
        }
        return o;
    }

    @Override
    public int getOrder() {
        return ResponseBodyOrderConstants.ENCRYPT;
    }
}
