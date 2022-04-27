package com.foco.mq.transactional.aspect;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.foco.mq.constant.MsgPropertyConstant;
import com.foco.mq.core.producer.MessageTransmitterHandlerMapping;
import com.foco.mq.mapper.SendRecordMapper;
import com.foco.mq.model.Msg;
import com.foco.mq.model.SendRecord;
import com.foco.mq.model.SendResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ChenMing
 * @date 2021/10/14
 */
@Aspect
@Slf4j
@Order(Integer.MIN_VALUE)
public class AfterTransactionAspect {

    /**
     * 当前线程积压的消息
     */
    private ThreadLocal<List<Msg>> messages = ThreadLocal.withInitial(LinkedList::new);

    /**
     * 当前链路
     */
    private ThreadLocal<AtomicInteger> link = ThreadLocal.withInitial(() -> new AtomicInteger(0));

    private MessageTransmitterHandlerMapping transmitterHandlerMapping;

    private SendRecordMapper recordMapper;

    public AfterTransactionAspect(ConfigurableListableBeanFactory beanFactory) {
        initializeProperty(beanFactory);
    }

    private void initializeProperty(ConfigurableListableBeanFactory beanFactory) {
        try {
            recordMapper = beanFactory.getBean(SendRecordMapper.class);
        } catch (NoSuchBeanDefinitionException ex) {
            log.warn(SendRecordMapper.class + " not found");
        }
        transmitterHandlerMapping = beanFactory.getBean(MessageTransmitterHandlerMapping.class);
    }

    /**
     * 引用计数：计算链路是否为最外节点
     * <p>
     * 只切@Transactional 会导致大量有事务方法额外有开销，但对于使用者可以做到无感知，否则需要额外增加一个注解反而对大家使用不友好
     * 性能上面其实增加耗时不到1毫秒
     */
    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            link.get().incrementAndGet();
            Object result = joinPoint.proceed();
            List<Msg> msgs = messages.get();
            if (!CollectionUtils.isEmpty(msgs) && link.get().get() == 1) {
                send(msgs);
            }
            return result;
        } catch (Exception e) {
            clear();
            throw e;
        } finally {
            if (link.get().get() == 1) {
                clear();
            } else {
                link.get().decrementAndGet();
            }
        }
    }

    private void send(List<Msg> msgs) {
        msgs.removeIf(msg -> {
            long timeout = Long.parseLong(msg.getProperties().get(MsgPropertyConstant.TIMEOUT));
            SendResult sendResult = transmitterHandlerMapping.getTransfer(msg).send(msg, timeout);
            if (recordMapper != null) {
                if (sendResult.isSucceed()) {
                    //发送成功则删除生产端消息，减少数据库空间占用
                    recordMapper.delete(Wrappers.lambdaQuery(SendRecord.class)
                            .eq(SendRecord::getKeys, msg.getKeys())
                            .eq(SendRecord::getType, 1));
                } else {
                    SendRecord update = new SendRecord();
                    update.setErrorMsg(JSONObject.toJSONString(sendResult.getResult()));
                    recordMapper.update(update, Wrappers.lambdaUpdate(SendRecord.class)
                            .eq(SendRecord::getKeys, msg.getKeys())
                            .eq(SendRecord::getType, 1));
                }
            }
            return true;
        });
    }

    private void clear() {
        link.remove();
        messages.remove();
    }

    public void add(Msg msg) {
        messages.get().add(msg);
    }
}
