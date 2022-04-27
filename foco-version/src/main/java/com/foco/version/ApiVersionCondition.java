package com.foco.version;

import cn.hutool.core.util.StrUtil;
import com.foco.properties.SystemConfig;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description 自定义API版本
 * @date 2021-06-29 17:09
 */
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
    private int apiVersion;

    public ApiVersionCondition(double version){
        BigDecimal a1 = new BigDecimal(Double.toString(version));
        BigDecimal b1 = new BigDecimal(Double.toString(100));
        BigDecimal result = a1.multiply(b1);// 相乘结果
        this.apiVersion =result.intValue();
    }
    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        // 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiVersionCondition(other.getApiVersion());
    }
    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        String var = request.getHeader(SystemConfig.getConfig().getApiVersionHead());
        if(StrUtil.isNotEmpty(var)){
            int version = (int) (Double.valueOf(var) * 100);
            if(version >= this.apiVersion) {
                return this;
            }
        }
        return null;
    }
    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        // 优先匹配最新的版本号
        return other.getApiVersion() - this.apiVersion;
    }
    public int getApiVersion() {
        return apiVersion;
    }
}
