package com.foco.crypt;

import cn.hutool.core.util.StrUtil;
import com.foco.crypt.properties.SignProperties;
import com.foco.crypt.core.SignRequest;
import com.foco.crypt.core.algorithm.sign.SignAlgorithm;
import com.foco.crypt.core.algorithm.sign.SignAlgorithmFactory;
import com.foco.crypt.core.provider.sign.SignConfigProvider;
import com.foco.model.constant.FocoErrorCode;
import com.foco.model.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @Description 加解密处理
 * @Author lucoo
 * @Date 2021/6/15 14:06
 **/
@Slf4j
public class SignHandler {
    @Autowired
    private SignConfigProvider provider;
    @Autowired
    private SignProperties defaultSignProperties;
     /**
      * @Description: 验签
      * @Param: deCryptContent 解密后的明文
      * @Return:
      * @Author: lucoo
      * @Date:  2021/1/7 18:03
     **/
    public void checkSign(SignRequest signRequest,String deCryptContent){
        if(!defaultSignProperties.getEnabled()){
            return ;
        }
        boolean skip = shouldSkip(signRequest.getUrl(),"output");
        if(skip){
            return;
        }
        if(SignProperties.SignModel.OUTPUT != defaultSignProperties.getSignModel()&&shouldHandler(signRequest)){
            SignProperties signProperties = provider.getSignProperties(signRequest);
            boolean checkSign = routerSignAlgorithm(signRequest, signProperties).checkSign(signProperties,signRequest,deCryptContent);
            if(!checkSign){
                SystemException.throwException(FocoErrorCode.CHECK_SIGN_ERROR);
            }
        }
    }
    public String sign(String content, SignRequest signRequest){
        if(!defaultSignProperties.getEnabled()){
            return content;
        }
        boolean skip = shouldSkip(signRequest.getUrl(),"input");
        if(skip){
            return content;
        }
        if (SignProperties.SignModel.INPUT != defaultSignProperties.getSignModel()&&shouldHandler(signRequest)) {
            SignProperties signProperties = provider.getSignProperties(signRequest);
            SignAlgorithm signAlgorithm = routerSignAlgorithm(signRequest, signProperties);
            return signAlgorithm.sign(signProperties,signRequest,content);
        }
        return null;
    }
    private boolean shouldHandler(SignRequest signRequest) {
        List<String> cryptList = defaultSignProperties.getSignUrls();
        String url = signRequest.getUrl();
        if (cryptList.size()!=0) {
            if (cryptList.contains(url) && SignProperties.FilterType.EXCLUDE == defaultSignProperties.getFilterType()) {
                return false;
            } else if (!cryptList.contains(url) && SignProperties.FilterType.INCLUDE == defaultSignProperties.getFilterType()) {
                return false;
            }
        }
        return true;
    }
    private boolean shouldSkip(String currentUrl,String currentModel){
        List<String> cryptModelUrls = defaultSignProperties.getSignModelUrls()
                .stream().map(url->
                        url.split(":")[0]
                ).collect(Collectors.toList());
        for(int i=0;i<cryptModelUrls.size();i++){
            if(cryptModelUrls.get(i).equals(currentUrl)){
                //特殊url 特殊配置
                String model=defaultSignProperties.getSignModelUrls().get(i).split(":")[1].toLowerCase(Locale.ROOT);
                if(!currentModel.equals(model)){
                    return true;
                }
            }
        }
        return false;
    }
    private SignAlgorithm routerSignAlgorithm(SignRequest signRequest, SignProperties signProperties) {
        String algorithm;
        if (StrUtil.isNotBlank(signRequest.getAlgorithm())) {
            algorithm = signRequest.getAlgorithm();
        } else {
            algorithm = signProperties.getAlgorithm().name();
        }
        return SignAlgorithmFactory.routerAlgorithm(algorithm);
    }
}
