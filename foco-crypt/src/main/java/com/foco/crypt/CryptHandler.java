package com.foco.crypt;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foco.crypt.properties.CryptProperties;
import com.foco.crypt.core.CryptRequest;
import com.foco.crypt.core.algorithm.crypt.CryptAlgorithm;
import com.foco.crypt.core.algorithm.crypt.CryptAlgorithmFactory;
import com.foco.crypt.core.provider.crypt.CryptConfigProvider;
import com.foco.model.ApiResult;
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
public class CryptHandler {
    @Autowired
    private CryptConfigProvider provider;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CryptProperties defaultCryptProperties;

    /**
     * 解密
     *
     * @param cryptRequest
     * @return
     */
    public String deCrypt(CryptRequest cryptRequest) {
        String content = cryptRequest.getContent();
        if (!defaultCryptProperties.getEnabled()) {
            return content;
        }
        boolean skip = shouldSkip(cryptRequest.getUrl(),"output");
        if(skip){
            return content;
        }
        if (CryptProperties.CryptModel.OUTPUT != defaultCryptProperties.getCryptModel()&&shouldHandler(cryptRequest)) {
            String deCryptContent = null;
            try {
                CryptProperties cryptProperties = provider.getCryptProperties(cryptRequest);
                log.info("密文:{},算法:{}",content,cryptProperties.getAlgorithm());
                deCryptContent = routerCryptAlgorithm(cryptRequest, cryptProperties).deCrypt(cryptProperties, content);
            } catch (Exception e) {
                SystemException.throwException(FocoErrorCode.DECRYPT_ERROR, e);
            }
            return deCryptContent;
        }
        return content;
    }
    /**
     * 加密
     *
     * @param obj
     * @param cryptRequest
     * @return
     */
    public Object enCrypt(Object obj, CryptRequest cryptRequest) {
        if (!defaultCryptProperties.getEnabled()) {
            return obj;
        }
        boolean skip = shouldSkip(cryptRequest.getUrl(),"input");
        if(skip){
            return obj;
        }
        if (CryptProperties.CryptModel.INPUT != defaultCryptProperties.getCryptModel() && shouldHandler(cryptRequest)) {
            CryptProperties cryptProperties = provider.getCryptProperties(cryptRequest);
            CryptAlgorithm cryptAlgorithm = routerCryptAlgorithm(cryptRequest, cryptProperties);
            try {
                /*if (obj instanceof CommonResponse) {
                    CommonResponse response = (CommonResponse) obj;
                    if (response.getData() != null) {
                        response.setData(cryptAlgorithm.enCrypt(cryptProperties, objectMapper.writeValueAsString(response.getData())));
                        return response;
                    }
                } else *//*if (obj instanceof String) {
                    //网关层接收到的只能是String
                    String obj1 = (String) obj;
                    CommonResponse response = null;
                    try {
                        response = JSON.parseObject(obj1, CommonResponse.class);
                    } catch (Exception e) {
                    }
                    if (response != null && response.getData() != null) {
                        response.setData(cryptAlgorithm.enCrypt(cryptProperties, objectMapper.writeValueAsString(response.getData())));
                        return response;
                    }
                }*/
                return ApiResult.success(cryptAlgorithm.enCrypt(cryptProperties, objectMapper.writeValueAsString(obj)));
            } catch (Exception e) {
                SystemException.throwException(FocoErrorCode.ENCRYPT_ERROR, e);
            }
        }
        return obj;
    }

    private boolean shouldHandler(CryptRequest cryptRequest) {
        List<String> cryptList = defaultCryptProperties.getCryptUrls();
        String url = cryptRequest.getUrl();
        if (cryptList.size() != 0) {
            if (cryptList.contains(url) && CryptProperties.FilterType.EXCLUDE == defaultCryptProperties.getFilterType()) {
                return false;
            } else if (!cryptList.contains(url) && CryptProperties.FilterType.INCLUDE == defaultCryptProperties.getFilterType()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取加解密算法
     * 优先取请求参数中指定的算法,
     * 没有指定则取配置的算法
     *
     * @param cryptRequest
     * @param cryptProperties
     * @return
     */
    private CryptAlgorithm routerCryptAlgorithm(CryptRequest cryptRequest, CryptProperties cryptProperties) {
        String algorithm;
        if (StrUtil.isNotBlank(cryptRequest.getAlgorithm())) {
            algorithm = cryptRequest.getAlgorithm();
        } else {
            algorithm = cryptProperties.getAlgorithm().name();
        }
        return CryptAlgorithmFactory.routerAlgorithm(algorithm);
    }
    private boolean shouldSkip(String currentUrl,String currentModel){
        List<String> cryptModelUrls = defaultCryptProperties.getCryptModelUrls()
                .stream().map(url->
                        url.split(":")[0]
                ).collect(Collectors.toList());
        for(int i=0;i<cryptModelUrls.size();i++){
            if(cryptModelUrls.get(i).equals(currentUrl)){
                //特殊url 特殊配置
                String model=defaultCryptProperties.getCryptModelUrls().get(i).split(":")[1].toLowerCase(Locale.ROOT);
                if(!currentModel.equals(model)){
                    return true;
                }
            }
        }
        return false;
    }
}
