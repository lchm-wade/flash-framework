package com.foco.crypt.inteceptor.gateway;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.foco.context.annotation.GatewayTrace;
import com.foco.properties.SystemConfig;
import com.foco.crypt.CryptHandler;
import com.foco.crypt.SignHandler;
import com.foco.crypt.properties.CryptProperties;
import com.foco.crypt.properties.SignProperties;
import com.foco.crypt.core.CryptRequest;
import com.foco.crypt.core.SignRequest;
import com.foco.model.ApiResult;
import com.foco.model.constant.FocoErrorCode;
import com.foco.model.constant.GatewayOrdersConstants;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @Description 响应 解密
 * @Author lucoo
 * @Date 2021/6/18 15:46
 **/
@GatewayTrace
public class ResponseBodyEnCryptFilter implements GlobalFilter, Ordered {
    @Autowired
    private CryptHandler cryptHandler;
    @Autowired
    private SignHandler signHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }
        ServerHttpResponse originalResponse = exchange.getResponse();
        final String[] sign = {""};
        final String[] source = {""};
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.buffer().map(dataBuffer -> {
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer join = dataBufferFactory.join(dataBuffer);
                        byte[] content = new byte[join.readableByteCount()];
                        join.read(content);
                        //释放掉内存
                        DataBufferUtils.release(join);
                        // 对响应体进行加密
                        // 私钥加密返回的响应体
                        CryptRequest cryptRequest = new CryptRequest();
                        cryptRequest.setAppId(request.getHeaders().getFirst(SystemConfig.getConfig().getAppId()));
                        cryptRequest.setAlgorithm(request.getHeaders().getFirst(CryptProperties.getConfig().getCryptAlgorithmHead()));
                        cryptRequest.setUrl(request.getPath().toString());
                        Object o;
                        String returnVal;
                        try {
                            source[0] = new String(content, StandardCharsets.UTF_8);
                            SignRequest signRequest = new SignRequest();
                            signRequest.setAppId(request.getHeaders().getFirst(SystemConfig.getConfig().getAppId()));
                            signRequest.setAlgorithm(request.getHeaders().getFirst(SignProperties.getConfig().getSignAlgorithmHead()));
                            signRequest.setUrl(request.getPath().toString());
                            signRequest.setMethod(request.getMethod().name());
                            sign[0] = signHandler.sign(source[0], signRequest);

                            o = cryptHandler.enCrypt(source[0], cryptRequest);
                            if (o instanceof ApiResult
                                    || (o instanceof String && !source[0].equals(String.valueOf(o)))) {
                                returnVal = JSON.toJSONString(o);
                            } else {
                                returnVal = String.valueOf(o);
                            }
                        } catch (Exception e) {
                            returnVal = JSON.toJSONString(ApiResult.error(FocoErrorCode.SYSTEM_ERROR.getCode(), e.getMessage()));
                        }
                        return dataBufferFactory.wrap(returnVal.getBytes(StandardCharsets.UTF_8));
                    }));
                }
                return super.writeWith(body);
            }
        };
        if (StrUtil.isNotBlank(sign[0]) && !source[0].equals(sign)) {
            decoratedResponse.getHeaders().add(SignProperties.getConfig().getSignHead(), sign[0]);
        }
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return GatewayOrdersConstants.DECRYPT_FILTER;
    }
}
