package com.foco.context.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * <p>描述：分布式自增长ID</p>
 * <pre>
 *     Twitter的 Snowflake　JAVA实现方案
 * </pre>
 * @Author lucoo
 * @Date 2021/6/28 11:16
 */
@Slf4j
public class SnowflakeIdWorker {
    /***时间起始[2020-01-01]标记点，作为基准，一般取系统的最近时间（一旦确定不能变动）*/
    private final static long START_STMP  = 1577808000000L;
    /***机器标识位数*/
    private final static long WORKER_ID_BITS = 5L;
    /***数据中心标识位数*/
    private final static long DATA_CENTER_ID_BITS = 5L;
    /***机器ID最大值*/
    private final static long MAX_WORKER_ID =  ~(-1L << WORKER_ID_BITS);
    /***数据中心ID最大值*/
    private final static long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    /***毫秒内自增位*/
    private final static long SEQUENCE_BITS = 12L;
    /***机器ID偏左移12位*/
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    /***数据中心ID左移17位*/
    private final static long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    /** 时间截向左移22位(5+5+12) */
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    /*** 上次生产id时间戳 */
    private static long lastTimestamp = -1L;
    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;
    /** 工作机器ID(0~31) */
    private final long workerId;
    /** 数据中心ID(0~31) */
    private final long dataCenterId;

    public SnowflakeIdWorker(){
        this.dataCenterId = getDataCenterId();
        this.workerId = getMaxWorkerId();
    }
    /**
     * @param workerId 工作机器ID
     * @param dataCenterId 序列号
     */
    public SnowflakeIdWorker(long workerId, long dataCenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATA_CENTER_ID));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }
    /**
     * 获取下一个ID
     * @return
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            // 当前毫秒内，则+1
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                // 当前毫秒内计数满了，则等待下一秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }
        //上次生成ID的时间截
        lastTimestamp = timestamp;
        /**
         * 返回结果：
         * (timestamp - START_STMP) << TIMESTAMP_LEFT_SHIFT) 表示将时间戳减去初始时间戳，再左移相应位数
         * (dataCenterId << DATA_CENTER_ID_SHIFT) 表示将数据id左移相应位数
         * (workerId << WORKER_ID_SHIFT) 表示将工作id左移相应位数
         * | 是按位或运算符，例如：x | y，只有当x，y都为0的时候结果才为0，其它情况结果都为1。
         * 因为个部分只有相应位上的值有意义，其它位上都是0，所以将各部分的值进行 | 运算就能得到最终拼接好的id
         */
        return ((timestamp - START_STMP) << TIMESTAMP_LEFT_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT) | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }
    /**
     * <p>
     * 获取 MAX_WORKER_ID
     * </p>
     */
    protected static long getMaxWorkerId() {
        StringBuilder mpId = new StringBuilder();
        mpId.append(MAX_WORKER_ID);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
            //GET jvmPid
            mpId.append(name.split("@")[0]);
        }
        //MAC + PID 的 hashcode 获取16个低位
        return (mpId.toString().hashCode() & 0xffff) % (MAX_WORKER_ID + 1);
    }

    /**
     * <p>
     * 数据标识id部分
     * </p>
     */
    protected static long getDataCenterId() {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if(mac!=null&&mac.length>0){
                    id = ((0x000000FF & (long) mac[mac.length - 1])
                            | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (MAX_DATA_CENTER_ID + 1);
                }else{
                    log.warn("mac is null || length=0");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return id;
    }
}
