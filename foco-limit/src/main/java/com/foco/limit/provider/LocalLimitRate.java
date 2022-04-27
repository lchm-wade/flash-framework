package com.foco.limit.provider;

import com.foco.limit.LimitParam;
import com.foco.limit.LimitRateSupport;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 本地jvm 限频
 * @Author lucoo
 * @Date 2021/6/29 16:19
 **/
public class LocalLimitRate implements LimitRateSupport {
    Map<String, LimitValue> map = new HashMap<>();
    public  Map<String, LimitValue> getMap(){
        return map;
    };
    public boolean limit(LimitParam limitParam) {
        String lockKey = limitParam.getKey().intern();
        String key=limitParam.getKey();
        synchronized (lockKey) {
            LimitValue value = map.get(key);
            if (value == null) {
                LimitValue param = new LimitValue();
                param.setCount(1);
                param.setTime(LocalDateTime.now().plusSeconds(limitParam.getLimitTime()));
                map.put(key, param);
            } else if(LocalDateTime.now().isAfter(value.getTime())){
                value.setCount(1);
                value.setTime(LocalDateTime.now().plusSeconds(limitParam.getLimitTime()));
            }else if (limitParam.getLimitCount() > value.getCount()) {
                value.setCount(value.getCount() + 1);
            } else {
                return true;
            }
        }
        return false;
    }

    @Getter
    @Setter
    class LimitValue {
        private int count;
        private LocalDateTime time;
    }
}
