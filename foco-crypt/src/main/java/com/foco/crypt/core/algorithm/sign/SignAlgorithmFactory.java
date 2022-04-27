package com.foco.crypt.core.algorithm.sign;

import com.foco.crypt.core.algorithm.Sign;
import com.foco.model.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/16 11:27
 **/
public class SignAlgorithmFactory {
    @Autowired
    private List<SignAlgorithm> signAlgorithmList;
    private static Map<String, SignAlgorithm> algorithmContext = new HashMap<>();

    @PostConstruct
    public void init() {
        for (SignAlgorithm signAlgorithm : signAlgorithmList) {
            Class<? extends SignAlgorithm> clazz = signAlgorithm.getClass();
            if (clazz.isAnnotationPresent(Sign.class)) {
                Sign sign = clazz.getAnnotation(Sign.class);
                String algorithmName = sign.algorithm().name().toUpperCase(Locale.ROOT);
                if(algorithmContext.containsKey(algorithmName)){
                    SystemException.throwException(String.format("SignAlgorithm子类算法名重复%s",algorithmName));
                }
                algorithmContext.put(algorithmName, signAlgorithm);
            }
        }
    }
    public static SignAlgorithm routerAlgorithm(String algorithm){
        if(!algorithmContext.containsKey(algorithm)){
            SystemException.throwException("algorithm:"+algorithm+"没有对应的算法类");
        }
        return algorithmContext.get(algorithm.toUpperCase(Locale.ROOT));
    }
}
