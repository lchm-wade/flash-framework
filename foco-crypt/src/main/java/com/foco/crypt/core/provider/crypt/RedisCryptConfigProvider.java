package com.foco.crypt.core.provider.crypt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foco.crypt.properties.CryptProperties;
import com.foco.context.util.BeanCopierEx;
import com.foco.crypt.core.CryptRequest;
import com.foco.crypt.core.provider.crypt.db.CryptInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/15 14:10
 **/
public class RedisCryptConfigProvider extends AbstractExternalStoreCryptConfigProvider {
@Autowired
private RedisTemplate redisTemplate;
@Autowired
private ObjectMapper objectMapper;
    @Override
    protected CryptProperties loadCryptConfig(CryptProperties cryptProperties, CryptRequest cryptRequest) {
        CryptInfo info = getObject(cryptProperties.getRedisPrefix()+ cryptRequest.getAppId(), CryptInfo.class);
        CryptProperties properties=new CryptProperties();
        BeanCopierEx.copyProperties(cryptProperties,properties);
        if(info!=null){
            BeanCopierEx.copyProperties(info,properties);
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
