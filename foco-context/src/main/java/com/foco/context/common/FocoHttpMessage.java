package com.foco.context.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.InputStream;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 16:49
 **/
public class FocoHttpMessage implements HttpInputMessage {
    private InputStream body;
    private HttpHeaders httpHeaders;

    public FocoHttpMessage(InputStream body, HttpHeaders httpHeaders) {
        this.body = body;
        this.httpHeaders = httpHeaders;
    }
    public FocoHttpMessage() {
    }
    @Override
    public InputStream getBody() {
        return this.body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.httpHeaders;
    }
}
