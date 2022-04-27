package com.foco.model.constant;

/**
 * @Description mybatis拦截器顺序定义
 * 值大的小执行，与spring拦截器相反
 * @Author lucoo
 * @Date 2021/6/30 18:53
 **/
public interface DbInterceptorOrderConstants {
    int shadowTableInterceptor=110;
    int safeSqlInterceptor=100;
    int logicDeleteInterceptor=90;
    int pageInterceptor=80;
    int mybatisPlusInterceptor= 70;
    int encryptInterceptor=60;
    int decryptInterceptor=50;
    int sqlLogInterceptor=1;
}
