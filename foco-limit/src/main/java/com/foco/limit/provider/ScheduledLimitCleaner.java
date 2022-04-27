package com.foco.limit.provider;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description: TODO
 * @Author lucoo
 * @Date 2021/6/30 15:22
 **/
@Slf4j
public class ScheduledLimitCleaner implements CommandLineRunner, DisposableBean {
    @Autowired
    private LocalLimitRate limitRate;
    private boolean cleaning=false;
    CleanTimeTask cleanTimeTask;
    @Override
    public void run(String... args) throws Exception {
        cleanTimeTask = new CleanTimeTask();
        new Timer().schedule(cleanTimeTask, 10000, 10000);
    }

    @Override
    public void destroy() throws Exception {
        cleanTimeTask.cancel();
    }

    class CleanTimeTask extends TimerTask {
        @Override
        public void run() {
            if(!cleaning){
                cleaning=true;
                Map map = limitRate.getMap();
                Iterator<LocalLimitRate.LimitValue> iterator = map.values().iterator();
                while (iterator.hasNext()) {
                    LocalLimitRate.LimitValue limitValue = iterator.next();
                    if (LocalDateTime.now().isAfter(limitValue.getTime().plusMinutes(5))) {
                        log.info("定时删除内存中的值:{}", JSON.toJSONString(limitValue));
                        iterator.remove();
                    }
                }
                cleaning=false;
            }
        }
    }
}
