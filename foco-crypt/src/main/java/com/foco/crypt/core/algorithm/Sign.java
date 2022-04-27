package com.foco.crypt.core.algorithm;

import com.foco.crypt.properties.SignProperties;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/16 11:55
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Sign {
    SignProperties.Algorithm algorithm();
}
