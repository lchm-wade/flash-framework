package com.foco.context.util;

public interface SerialNumberHelper {
    /**
     * 生成 210803130000001
     * yyMMddHH+8位自增的流水号
     * @param prefix
     * @return
     */
    String generateByHour(String prefix);
    /**
     * 生成 210803130000001
     * yyMMddHH+count位自增的流水号
     * @param prefix
     * @return
     */
    String generateByHour(String prefix,int count);
    /**
     * 生成 20210803130000001
     * yyyyMMddHH+8位自增的流水号
     * @param prefix
     * @return
     */
    String generateFullByHour(String prefix);

    /**
     * 生成 20210803130000001
     * yyyyMMddHH+count位自增的流水号
     * @param prefix
     * @return
     */
    String generateFullByHour(String prefix,int count);
    /**
     * 生成 2108030000001
     * yyMMdd+8位自增的流水号
     * @param prefix
     * @return
     */
    String generateByDay(String prefix);

    /**
     * 生成 2108030000001
     * yyMMdd+count位自增的流水号
     * @param prefix
     * @return
     */
    String generateByDay(String prefix,int count);
    /**
     * 生成 202108030000001
     * yyyyMMdd+8位自增的流水号
     * @param prefix
     * @return
     */
    String generateFullByDay(String prefix);

    /**
     * 生成 202108030000001
     * yyyyMMdd+count位自增的流水号
     * @param prefix
     * @return
     */
    String generateFullByDay(String prefix,int count);
    /**
     * 生成 210803130000001
     * yyMMddHH+8位自增的流水号
     * @param prefix
     * @return
     */
    String generateByMonth(String prefix);
    /**
     * 生成 2108030000001
     * yyMMdd+count位自增的流水号
     * @param prefix
     * @return
     */
    String generateByMonth(String prefix,int count);
    /**
     * 生成 20210803130000001
     * yyyyMMddHH+8位自增的流水号
     * @param prefix
     * @return
     */
    String generateFullByMonth(String prefix);
    /**
     * 生成 202108030000001
     * yyyyMMdd+count位自增的流水号
     * @param prefix
     * @return
     */
    String generateFullByMonth(String prefix,int count);
    /**
     * 生成递增的编码，比如项目编码
     * @param prefix A
     * @param count 8
     * 生成 A00000001
     */
    String generateNumber(String prefix,int count);
}
