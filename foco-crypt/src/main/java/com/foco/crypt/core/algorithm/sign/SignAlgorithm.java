package com.foco.crypt.core.algorithm.sign;

import com.foco.crypt.properties.SignProperties;
import com.foco.crypt.core.SignRequest;

/**
 * @Description: TODO
 * @Author lucoo
 * @Date 2021/1/6 15:02
 **/
public interface SignAlgorithm {
    boolean checkSign(SignProperties signProperties, SignRequest signRequest, String content);
    String sign(SignProperties signProperties, SignRequest signRequest, String content);
}
