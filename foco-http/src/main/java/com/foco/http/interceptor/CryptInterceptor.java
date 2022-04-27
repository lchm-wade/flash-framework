package com.foco.http.interceptor;

import cn.hutool.core.util.StrUtil;
import com.foco.properties.SystemConfig;
import com.foco.crypt.properties.SignProperties;
import com.foco.crypt.CryptHandler;
import com.foco.crypt.SignHandler;
import com.foco.crypt.core.CryptRequest;
import com.foco.crypt.core.SignRequest;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.BaseGlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @Description: TODO
 * @Author lucoo
 * @Date 2021/1/11 15:31
 **/
@Slf4j
public class CryptInterceptor extends BaseGlobalInterceptor {
    @Autowired
    private CryptHandler cryptHandler;
    @Autowired
    private SignHandler signHandler;

    @Override
    protected Response doIntercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = enCryptThenRebuildRequest(request);
        Response response = chain.proceed(newRequest);
        Response newResponse = deCryptThenRebuildResponse(request, response);
        return newResponse;
    }

    private Request enCryptThenRebuildRequest(Request request) {
        if ("POST".equals(request.method())) {
            RequestBody originalRequestBody = request.body();
            String paramContent = getParamContent(originalRequestBody);
            if (StrUtil.isNotBlank(paramContent)) {
                //加密
                Object enCrypt = enCrypt(paramContent, request);
                RequestBody newRequestBody = RequestBody.create(originalRequestBody.contentType(), String.valueOf(enCrypt));
                //加签
                Object sign = sign(paramContent, request);
                Request newRequest = request.newBuilder()
                        .addHeader(SignProperties.getConfig().getSignHead(), String.valueOf(sign))
                        .method(request.method(), newRequestBody)
                        .build();
                return newRequest;
            }
        }
        return request;
    }

    private Object enCrypt(String body, Request request) {
        CryptRequest cryptRequest = new CryptRequest();
        cryptRequest.setUrl(request.url().encodedPath());
        cryptRequest.setAlgorithm(request.header(SignProperties.getConfig().getSignAlgorithmHead()));
        cryptRequest.setAppId(request.header(SystemConfig.getConfig().getAppId()));
        return cryptHandler.enCrypt(body, cryptRequest);
    }

    private Response deCryptThenRebuildResponse(Request request, Response response) {
        ResponseBody originalResponseBody = response.body();
        try {
            String sourceBody = originalResponseBody.string();
            //解密
            String deCrypt = deCrypt(request, sourceBody);
            //验签
            checkSign(request, deCrypt, response.header(SignProperties.getConfig().getSignHead()));
            ResponseBody newResponseBody = ResponseBody.create(originalResponseBody.contentType(), String.valueOf(deCrypt));
            return response.newBuilder()
                    .body(newResponseBody)
                    .build();
        } catch (IOException e) {
            log.error("读取请求响应数据异常",e);
        }
        return response;
    }

    private String deCrypt(Request request, String content) {
        CryptRequest cryptRequest = new CryptRequest();
        cryptRequest.setUrl(request.url().encodedPath());
        cryptRequest.setAlgorithm(request.header(SignProperties.getConfig().getSignAlgorithmHead()));
        cryptRequest.setAppId(request.header(SystemConfig.getConfig().getAppId()));
        cryptRequest.setContent(content);
        return cryptHandler.deCrypt(cryptRequest);
    }

    private boolean checkSign(Request request, String deCryptContent, String sign) {
        SignRequest signRequest = new SignRequest();
        signRequest.setUrl(request.url().encodedPath());
        signRequest.setAlgorithm(request.header(SignProperties.getConfig().getSignAlgorithmHead()));
        signRequest.setAppId(request.header(SystemConfig.getConfig().getAppId()));
        signRequest.setSign(sign);
        signRequest.setMethod(request.method());
        signHandler.checkSign(signRequest, deCryptContent);
        return true;
    }

    private Object sign(String body, Request request) {
        SignRequest signRequest = new SignRequest();
        signRequest.setUrl(request.url().encodedPath());
        signRequest.setAlgorithm(request.header(SignProperties.getConfig().getSignAlgorithmHead()));
        signRequest.setAppId(request.header(SystemConfig.getConfig().getAppId()));
        return signHandler.sign(body, signRequest);
    }

    /**
     * 获取常规post请求参数
     */
    private String getParamContent(RequestBody body) {
        Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException e) {
            log.error("获取请求参数异常", e);
        }
        return null;
    }
}
