package com.foco.crypt.core.algorithm.crypt;

import com.foco.crypt.core.algorithm.Crypt;
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
public class CryptAlgorithmFactory {
    @Autowired
    private List<CryptAlgorithm> cryptCryptAlgorithmList;
    private static Map<String,CryptAlgorithm> algorithmContext = new HashMap<>();

    @PostConstruct
    public void init() {
        for (CryptAlgorithm cryptAlgorithm : cryptCryptAlgorithmList) {
            Class<? extends CryptAlgorithm> clazz = cryptAlgorithm.getClass();
            if (clazz.isAnnotationPresent(Crypt.class)) {
                Crypt algorithm = clazz.getAnnotation(Crypt.class);
                String algorithmName = algorithm.algorithm().name().toUpperCase(Locale.ROOT);
                if(algorithmContext.containsKey(algorithmName)){
                    SystemException.throwException(String.format("CryptAlgorithm子类算法名重复%s",algorithmName));
                }
                algorithmContext.put(algorithmName, cryptAlgorithm);
            }
        }
    }
    public static CryptAlgorithm routerAlgorithm(String algorithm){
        if(!algorithmContext.containsKey(algorithm)){
            SystemException.throwException("algorithm:"+algorithm+"没有对应的算法类");
        }
        return algorithmContext.get(algorithm.toUpperCase(Locale.ROOT));
    }
}
