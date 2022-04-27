package com.foco.distributed.id.generate;

import cn.hutool.core.util.StrUtil;
import com.foco.context.core.SpringContextHolder;
import com.foco.context.util.SerialNumberUtil;
import com.foco.distributed.id.generate.cycle.SegmentData;
import com.foco.model.constant.FocoErrorCode;
import com.foco.model.exception.SystemException;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/08/30 18:18
 */
public class IdGeneratorUtil {
    private static SegmentGeneratorFactory segmentGeneratorFactory=SpringContextHolder.getBean(SegmentGeneratorFactory.class);
    /**
     * 前缀+id
     */
    public static String generateWithPrefix(String bizTag) {
        return new StringBuilder()
                .append(bizTag)
                .append(generate(bizTag))
                .toString();
    }

    /**
     * 前缀+id(id补0)
     */
    public static String generateWithPrefix(String bizTag, int count) {
        return new StringBuilder()
                .append(bizTag)
                .append(generate(bizTag, count))
                .toString();
    }

    /**
     * 只生成id(id补0)
     */
    public static String generate(String bizTag, int count) {
        if (count < 0) {
            SystemException.throwException(FocoErrorCode.PARAMS_VALID.getCode(),"count less than 0");
        }
        return SerialNumberUtil.splice(generate(bizTag), count);
    }

    /**
     * 只生成id
     */
    public static long generate(String bizTag) {
        if (StrUtil.isBlank(bizTag)) {
            SystemException.throwException(FocoErrorCode.PARAMS_VALID.getCode(), "bizTag is null");
        }
        return segmentGeneratorFactory.generateNo(bizTag);
    }

    /**
     * 前缀+生成时间+id
     */
    public static String generateByTimeWithPrefix(String bizTag) {
        return new StringBuilder().append(bizTag)
                .append(generateByTime(bizTag))
                .toString();
    }

    /**
     * 前缀+生成时间+id(id补0)
     */
    public static String generateByTimeWithPrefix(String bizTag, int count) {
        return new StringBuilder().append(bizTag)
                .append(generateByTime(bizTag, count))
                .toString();
    }

    /**
     * 生成时间+id(id补0)
     */
    public static String generateByTime(String bizTag, int count) {
        if (count < 0) {
            SystemException.throwException(FocoErrorCode.PARAMS_VALID.getCode(),"count less than 0");
        }
        SegmentData segmentData = doGenerateByTime(bizTag);
        return new StringBuilder().append(segmentData.getTime())
                .append(SerialNumberUtil.splice(segmentData.getResult(), count))
                .toString();
    }

    /**
     * 生成时间+id
     */
    public static String generateByTime(String bizTag) {
        SegmentData segmentData = doGenerateByTime(bizTag);
        return new StringBuilder().append(segmentData.getTime())
                .append(segmentData.getResult())
                .toString();
    }

    private static SegmentData doGenerateByTime(String bizTag) {
        if (StrUtil.isBlank(bizTag)) {
            SystemException.throwException(FocoErrorCode.PARAMS_VALID.getCode(),"bizTag is null");
        }
        return segmentGeneratorFactory.generateByTime(bizTag);
    }
}
