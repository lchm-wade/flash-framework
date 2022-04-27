package com.foco.limit.provider;

import com.foco.context.common.RedisPrefix;
import com.foco.limit.LimitParam;
import com.foco.limit.LimitRateSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.List;

/**
 * @Description: redis 限频
 * @Author lucoo
 * @Date 2021/6/29 16:19
 **/
public class RedisLimitRate implements LimitRateSupport {
    @Autowired
    private RedisPrefix redisPrefix;
    @Autowired
    private DefaultRedisScript redisLimit;
    @Autowired
    private  RedisTemplate redisTemplate;
    public boolean limit(LimitParam limitParam){
        String key=redisPrefix.getPrefix()+limitParam.getKey();
        List<String> keys = Collections.singletonList(key);
        return (long) redisTemplate.execute(redisLimit, keys, limitParam.getLimitTime(),limitParam.getLimitCount())==0L;
    }
}
