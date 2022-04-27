package com.foco.crypt.inteceptor.gateway;

import com.foco.context.annotation.GatewayTrace;
import com.foco.properties.SystemConfig;
import com.foco.context.util.XssShieldUtil;
import com.foco.crypt.CryptHandler;
import com.foco.crypt.SignHandler;
import com.foco.crypt.properties.CryptProperties;
import com.foco.crypt.properties.SignProperties;
import com.foco.crypt.core.CryptRequest;
import com.foco.crypt.core.SignRequest;
import com.foco.model.constant.GatewayOrdersConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @Description 请求参数 解密
 * @Author lucoo
 * @Date 2021/6/18 15:46
 **/
@Slf4j
@GatewayTrace
public class RequestBodyDeCryptFilter implements GlobalFilter, Ordered {
    @Autowired
    private CryptHandler cryptHandler;
    @Autowired
    private SignHandler signHandler;
    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }
        ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);
        HttpHeaders headers = request.getHeaders();
        MediaType mediaType = headers.getContentType();
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                .flatMap(body -> {
                    if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
                        CryptRequest cryptRequest = new CryptRequest();
                        cryptRequest.setContent(body);
                        cryptRequest.setAppId(headers.getFirst(SystemConfig.getConfig().getAppId()));
                        cryptRequest.setAlgorithm(headers.getFirst(CryptProperties.getConfig().getCryptAlgorithmHead()));
                        cryptRequest.setUrl(request.getPath().toString());
                        String deCrypt = cryptHandler.deCrypt(cryptRequest);

                        SignRequest signRequest = new SignRequest();
                        signRequest.setAppId(headers.getFirst(SystemConfig.getConfig().getAppId()));
                        signRequest.setAlgorithm(headers.getFirst(SignProperties.getConfig().getSignAlgorithmHead()));
                        signRequest.setUrl(request.getPath().toString());
                        signRequest.setSign(headers.getFirst(SignProperties.getConfig().getSignHead()));
                        signRequest.setMethod(request.getMethod().name());
                        signHandler.checkSign(signRequest, deCrypt);
                        return Mono.just(XssShieldUtil.stripXss(deCrypt));
                    }
                    return Mono.empty();
                });
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders newHeaders = new HttpHeaders();
        newHeaders.putAll(headers);
        newHeaders.remove(HttpHeaders.CONTENT_LENGTH);
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, newHeaders);
        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> {
                    ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
                            request) {
                        @Override
                        public HttpHeaders getHeaders() {
                            long contentLength = newHeaders.getContentLength();
                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.putAll(super.getHeaders());
                            if (contentLength > 0) {
                                httpHeaders.setContentLength(contentLength);
                            } else {
                                httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                            }
                            return httpHeaders;
                        }

                        @Override
                        public Flux<DataBuffer> getBody() {
                            return outputMessage.getBody();
                        }
                    };
                    return chain.filter(exchange.mutate().request(decorator).build());
                }));
    }
    @Override
    public int getOrder() {
        return GatewayOrdersConstants.REQUEST_FILTER;
    }
}
