package com.foco.context.util;

/**
 * @Description md5加盐加密
 * 盐值通过算法计算,无需额外存储
 * @Author lucoo
 * @Date 2021/6/24 14:18
 **/

import cn.hutool.crypto.SecureUtil;

import java.util.Random;

public class Md5Utils {

    /**
     * 加盐MD5加密
     */
    public static String getMD5Str(String password) {
        // 生成一个16位的随机数
        Random random = new Random();
        StringBuilder sBuilder = new StringBuilder(16);
        sBuilder.append(random.nextInt(99999999)).append(random.nextInt(99999999));
        int len = sBuilder.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sBuilder.append("0");
            }
        }
        // 生成最终的加密盐
        String salt = sBuilder.toString();
        password = SecureUtil.md5(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return String.valueOf(cs);
    }
    /**
     * 验证加盐后密码是否还相同
     * @param password
     * @param md5str
     * @return
     */
    public static boolean checkMd5(String password, String md5str) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5str.charAt(i);
            cs1[i / 3 * 2 + 1] = md5str.charAt(i + 2);
            cs2[i / 3] = md5str.charAt(i + 1);
        }
        String Salt = new String(cs2);
        return SecureUtil.md5(password + Salt).equals(String.valueOf(cs1));
    }

    public static void main(String[] args) {
        System.out.println(getMD5Str("123456"));
        System.out.println(checkMd5("123456","f73731026735f3ac41137c3e25037382f71144de8ae3af45"));
    }
}
