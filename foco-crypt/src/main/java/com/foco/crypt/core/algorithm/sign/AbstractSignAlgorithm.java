package com.foco.crypt.core.algorithm.sign;

import cn.hutool.core.util.StrUtil;
import com.foco.crypt.properties.SignProperties;
import com.foco.context.core.SpringContextHolder;
import com.foco.crypt.core.SignRequest;
import com.foco.model.constant.MainClassConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ClassUtils;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author lucoo
 * @date 2021/1/28 9:24
 */
@Slf4j
public abstract class AbstractSignAlgorithm implements SignAlgorithm {
    protected abstract boolean doCheckSign(String sign,String content, SignProperties signProperties);

    protected abstract String doSign(String content, SignProperties signProperties);

    public boolean checkSign(SignProperties signProperties, SignRequest signRequest, String content) {
        //防重放攻击
        String sign=signRequest.getSign();
        if(StrUtil.isBlank(sign)){
            log.error("验签不通过,sign参数不能为空");
            return false;
        }
        String[] split = sign.split(";");
        if(split.length!=3){
            log.error("验签不通过,sign格式不正确");
            return false;
        }
        String timeStamp = split[0];
        String nonceStr = split[1];
        String signTarget=split[2];
        Long timeStampVal;
        if(StrUtil.isBlank(nonceStr)||StrUtil.isBlank(timeStamp)){
            log.error("验签不通过,timeStamp或者nonceStr参数不能为空");
            return false;
        }
        try {
            timeStampVal=Long.valueOf(timeStamp);
            if(timeStampVal==0){
                log.error("验签不通过,timeStamp不能为0");
                return false;
            }
        } catch (NumberFormatException e) {
            log.error("验签不通过,timeStamp格式不正确");
            return false;
        }
        if(nonceStr.length()!=32){
            log.error("验签不通过,nonceStr长度必须为32位");
            return false;
        }
        long diff = Math.abs(System.currentTimeMillis() - timeStampVal);
        long ignoreMillis = SignProperties.getConfig().getIgnoreMillis();
        if(diff > ignoreMillis){
            log.error("验签不通过,timeStamp超过服务器允许的时间");
            return false;
        }
        if(ClassUtils.isPresent(MainClassConstant.SPRING_DATA_REDIS,this.getClass().getClassLoader())){
            RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
            if(!redisTemplate.opsForValue().setIfAbsent(":sign:"+nonceStr,1,ignoreMillis, TimeUnit.MILLISECONDS)){
                log.error("验签不通过,nonceStr重复");
                return false;
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append(signRequest.getUrl())
                .append("\n")
                .append(signRequest.getMethod().toUpperCase(Locale.ROOT))
                .append("\n")
                .append(timeStamp)
                .append("\n")
                .append(nonceStr)
                .append("\n")
                .append(content);
        String signSource = builder.toString();
        log.info("待签名原串:{}",signSource);
        return doCheckSign(signTarget,signSource,signProperties);
    }

    public String sign(SignProperties signProperties, SignRequest signRequest, String content) {
        StringBuilder builder = new StringBuilder();
        String nonceStr = UUID.randomUUID().toString().replaceAll("-","");
        String timeStamp = String.valueOf(System.currentTimeMillis());
        builder.append(signRequest.getUrl())
                .append("\n")
                .append(signRequest.getMethod().toUpperCase(Locale.ROOT))
                .append("\n")
                .append(timeStamp)
                .append("\n")
                .append(nonceStr)
                .append("\n")
                .append(content);
        String signSource = builder.toString();
        log.info("待加签的原串:{}",signSource);
        String sign = doSign(signSource, signProperties);
        return timeStamp+";"+nonceStr+";"+sign;
    }
}
