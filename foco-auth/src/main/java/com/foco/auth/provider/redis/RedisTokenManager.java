package com.foco.auth.provider.redis;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foco.auth.common.AbstractTokenManager;
import com.foco.auth.properties.TokenProperties;
import com.foco.context.common.RedisPrefix;
import com.foco.context.core.SpringContextHolder;
import com.foco.properties.SystemConfig;
import com.foco.context.core.GenericLoginContext;
import com.foco.context.core.LoginContext;
import com.foco.context.core.LoginContextHolder;
import com.foco.context.util.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/1 19:54
 **/
public class RedisTokenManager extends AbstractTokenManager {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public <T extends LoginContext> T parseToken(String key, Class<T> loginContext) {
        return getObject(key, loginContext);
    }
    @Override
    public void refreshToken(String key, GenericLoginContext loginContext) {
        redisTemplate.opsForValue().set(key, loginContext, loginContext.getExpireTimeInSecond(), TimeUnit.SECONDS);
    }
    @Override
    public boolean updateToken(GenericLoginContext loginContext) {
        redisTemplate.opsForValue().set(buildKey(), loginContext, loginContext.getExpireTimeInSecond(), TimeUnit.SECONDS);
        return true;
    }
    private String buildKey(){
        String userId = LoginContextHolder.currentUserId();
        String clientType = HttpContext.getHeader(TokenProperties.getConfig().getClientTypeHead());
        if(StrUtil.isBlank(clientType)){
            clientType="none";
        }
        String token=HttpContext.getHeader(SystemConfig.getConfig().getTokenHead());
        return buildKey(userId,clientType,token);
    }
    @Override
    public boolean removeToken() {
        removePrefix(buildKey());
        return false;
    }

    @Override
    public void saveToken(GenericLoginContext loginContext, String token) {
        //删除同一客户端的用户登录信息
        String key = loginContext.getUserId() + SEPARATOR + loginContext.getClientType() + SEPARATOR + token;
        removePrefix(loginContext.getUserId() + SEPARATOR + loginContext.getClientType());
        //存储token
        redisTemplate.opsForValue().set(key, loginContext, loginContext.getExpireTimeInSecond(), TimeUnit.SECONDS);
    }

    /**
     * 删除指定前缀的所有数据
     *
     * @param prefix 前缀
     * @return 成功删除的数据条数
     */
    private int removePrefix(String prefix) {
        String finalPrefix=prefix;
        int prefixLen=0;
        if(ClassUtils.isPresent("com.foco.redis.support.RedisPrefix",this.getClass().getClassLoader())){
            RedisPrefix redisPrefix = SpringContextHolder.getBean(RedisPrefix.class);
            String focoRedisPrefix=redisPrefix.getPrefix();
            finalPrefix= focoRedisPrefix+finalPrefix;
            prefixLen=focoRedisPrefix.length();
        }
        Set<String> keys = scan(finalPrefix);
        for(String key:keys){
            redisTemplate.delete(key.substring(prefixLen));
        }
        int size = keys.size();
        return size;
    }
    private Set<String> scan(String matchKey) {
        Set<String> keys = (Set<String>) redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(matchKey + "*").count(1000).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            return keysTmp;
        });
        return keys;
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
