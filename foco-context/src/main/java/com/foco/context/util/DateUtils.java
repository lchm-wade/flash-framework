package com.foco.context.util;

import com.foco.model.constant.TimePattern;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/24 16:57
 **/
public class DateUtils {

    /**
     * 转换成日期格式 yyyy-MM-dd HH:mm:ss
     *
     * @return 例:2019-8-30
     */
    public static String dateToStringD(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TimePattern.YYYY_MM_DD_HH_MM_SS.getValue());
        return simpleDateFormat.format(date);
    }

    /**
     * 获取当前日期 yyyy-MM-dd HH:mm:ss
     *
     * @return 例:2019-8-30 11:03
     */
    public static String getNowDateDetail() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TimePattern.YYYY_MM_DD_HH_MM_SS.getValue());
        return simpleDateFormat.format(date);
    }

    public static boolean isToday(LocalDate localDate) {
        return LocalDate.now().equals(localDate);
    }

    public static boolean isToday(LocalDateTime localDate) {
        return LocalDate.now().equals(localDate.toLocalDate());
    }
}
