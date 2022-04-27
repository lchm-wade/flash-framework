package com.foco.context.util;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>
 *    分布式全局唯一ID生成器（支持自增）
 * </p>
 * @Author lucoo
 * @Date 2021/6/28 11:16
 */
@Slf4j
public class IdGenerator {

    private static final SnowflakeIdWorker ID_WORKER;

    static {
        ID_WORKER = new SnowflakeIdWorker();
    }
    /**
     * 根据雪花算法，生成分布式唯一ID
     */
    public static long createId() {
        return ID_WORKER.nextId();
    }
    /**
     * 根据当前时间生成流水号： 时间+5位随机数
     */
    @Deprecated
    public static String createNo(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String dateTime = LocalDateTime.now(ZoneOffset.of("+8")).format(formatter);
        String randomStr = RandomUtil.randomString(4);
        return dateTime+randomStr;
    }
    public static long createId(long workerId, long dataCenterId) {
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(workerId, dataCenterId);
        return snowflakeIdWorker.nextId();
    }
    /**
     * 批量获取
     */
    public static List<Long> batchCreateId(int size) {
        Set<Long> result = IntStream.range(0, size).mapToObj(i -> ID_WORKER.nextId()).collect(Collectors.toCollection(() -> new HashSet<>(size)));
        return new ArrayList<>(result);
    }
    /**
     * 批量获取
     */
    public static List<Long> batchCreateId(long workerId, long dataCenterId, int size) {
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(workerId, dataCenterId);
        Set<Long> result = IntStream.range(0, size).mapToObj(i -> snowflakeIdWorker.nextId()).collect(Collectors.toCollection(() -> new HashSet<>(size)));
        return new ArrayList<>(result);
    }
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 200; i++) {
            System.out.println(IdGenerator.createId());
        }
        System.out.println(System.currentTimeMillis() - start);


    }
}
