package com.foco.context.util;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: XXS 漏洞攻击处理
 *
 * @Author lucoo
 * @Date 2021/6/24 11:17
 */
public class XXSFilterUtil {

    /**
     * XXSFilterUtil
     */
    private XXSFilterUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * specialStrRegex
     */
    //private static String specialStrRegex = "[`~!@#$%^&*()\\+\\=\\{}|:\"?><【】\\/r\\/n]";
    private static String specialStrRegex = "[`~!@#$%^&*()\\+\\=|?><【】]";

    /**
     * filerSpecialChar
     *
     * @param input
     * @return
     */
    public static String filerSpecialChar(String input) {
        String encode = Normalizer.normalize(input, Normalizer.Form.NFKC);
        Pattern pattern = Pattern.compile(specialStrRegex);
        Matcher ma = pattern.matcher(encode);
        if (ma.find()) {
            encode = ma.replaceAll("");
        }
        return encode;
    }
}
