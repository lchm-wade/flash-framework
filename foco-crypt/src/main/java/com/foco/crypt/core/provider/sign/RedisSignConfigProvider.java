package com.foco.crypt.core.provider.sign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foco.crypt.properties.SignProperties;
import com.foco.context.util.BeanCopierEx;
import com.foco.crypt.core.SignRequest;
import com.foco.crypt.core.provider.sign.db.SignInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 14:10
 **/
public class RedisSignConfigProvider extends AbstractExternalStoreSignConfigProvider {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected SignProperties loadSignConfig(SignProperties signProperties, SignRequest signRequest) {
        SignInfo info = getObject(signProperties.getRedisPrefix() + signRequest.getAppId(), SignInfo.class);
        SignProperties properties = new SignProperties();
        BeanCopierEx.copyProperties(signProperties, properties);
        if (info != null) {
            BeanCopierEx.copyProperties(info, properties);
        }
        return properties;
    }
    public <T> T getObject(String key, Class<T> type) {
        Object result;
        result = redisTemplate.opsForValue().get(key);
        if (result == null) {
            return null;
        }
        try {
            return this.objectMapper.readValue(this.objectMapper.writeValueAsBytes(result), type);
        } catch (IOException e) {
            return null;
        }
    }
}