package com.foco.crypt.inteceptor.web;

import com.foco.properties.SystemConfig;
import com.foco.context.annotation.ConditionalOnMissingCloud;
import com.foco.context.common.FocoHttpMessage;
import com.foco.context.util.HttpContext;
import com.foco.context.util.XssShieldUtil;
import com.foco.crypt.CryptHandler;
import com.foco.crypt.SignHandler;
import com.foco.crypt.properties.CryptProperties;
import com.foco.crypt.properties.SignProperties;
import com.foco.crypt.core.CryptRequest;
import com.foco.crypt.core.SignRequest;
import com.foco.model.constant.RequestBodyOrderConstants;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Description 解密
 * @Author lucoo
 * @Date 2021/6/15 16:26
 **/
@RestControllerAdvice
@ConditionalOnMissingCloud
public class DeCryptRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter implements Ordered {
    @Autowired
    private CryptHandler cryptHandler;
    @Autowired
    private SignHandler signHandler;
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
            throws IOException {
        String httpBody= StreamUtils.copyToString(inputMessage.getBody(), Charset.defaultCharset());
        CryptRequest request=new CryptRequest();
        request.setContent(httpBody);
        request.setAppId(HttpContext.getHeader(SystemConfig.getConfig().getAppId()));
        request.setAlgorithm(HttpContext.getHeader(CryptProperties.getConfig().getCryptAlgorithmHead()));
        request.setUrl(HttpContext.getRequest().getServletPath());
        String deCrypt = cryptHandler.deCrypt(request);

        SignRequest signRequest=new SignRequest();
        signRequest.setAppId(HttpContext.getHeader(SystemConfig.getConfig().getAppId()));
        signRequest.setUrl(HttpContext.getRequest().getServletPath());
        signRequest.setAlgorithm(HttpContext.getHeader(SignProperties.getConfig().getSignAlgorithmHead()));
        signRequest.setSign(HttpContext.getHeader(SignProperties.getConfig().getSignHead()));
        signRequest.setMethod(HttpContext.getRequest().getMethod());
        signHandler.checkSign(signRequest,deCrypt);

        return new FocoHttpMessage(new ByteArrayInputStream(XssShieldUtil.stripXss(deCrypt).getBytes()), inputMessage.getHeaders());
    }
    @Override
    public int getOrder() {
        return RequestBodyOrderConstants.DECRYPT;
    }
}
