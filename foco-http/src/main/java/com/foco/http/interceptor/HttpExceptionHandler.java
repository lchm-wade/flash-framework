package com.foco.http.interceptor;

import com.foco.model.ApiResult;
import com.foco.model.constant.ExceptionHandlerOrderConstants;
import com.foco.model.constant.FocoErrorCode;
import com.github.lianjiatech.retrofit.spring.boot.exception.RetrofitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description 全局异常处理
 * @Author lucoo
 * @Date 2021/6/23 18:55
 **/
@Slf4j
@RestControllerAdvice
@Order(ExceptionHandlerOrderConstants.HTTP)
public class HttpExceptionHandler {
    /***
     *  Retrofit http异常拦截
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = RetrofitException.class)
    public ApiResult focoExceptionHandler(RetrofitException e) {
        log.error("---retrofitExceptionHandler Handler ERROR: {}", e.getMessage(),e);
        String message=e.getMessage().split("body=")[1];
        ApiResult apiResult = ApiResult.error(FocoErrorCode.EXTERNAL_ERROR.getCode(), message);
        return apiResult;
    }
}
