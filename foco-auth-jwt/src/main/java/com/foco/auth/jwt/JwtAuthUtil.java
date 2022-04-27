/**
 * zhengwenbo
 */
package com.foco.auth.jwt;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

/**
 * Jwt 工具类 https://github.com/jwtk
 * https://jwt.io/
 *
 * @author 程昭斌
 * @version 1.0
 * @date 2019/5/21 17:26
 */
@Slf4j
public final class JwtAuthUtil {

    private static final ZoneId ZONE = ZoneId.systemDefault();

    private JwtAuthUtil() {

    }
    /**
     * 生成一个令牌
     *
     * @param issuer         签发者
     * @param subject        主题内容
     * @param base64Security 签名key
     * @param second         过期时间(秒)
     * @return
     */
    public static String createJWT(String issuer, String subject, String base64Security, long second) {
        //获取当前服务器时间，并允许误差3分钟，处理服务器时间不同步
        LocalDateTime localDateTime = LocalDateTime.now();
        //创建时间
        Date now = toDate(localDateTime.minusMinutes(3));
        // 添加构成JWT的参数
        JwtBuilder builder = Jwts.builder()
                //签发者
                .setIssuer(issuer)
                // jwt的签发时间
                .setIssuedAt(now)
                // 主题
                .setSubject(subject)
                // 设置签名
                .signWith(generalKey(base64Security), SignatureAlgorithm.HS256);
        // 添加Token过期时间
        if (second > 0) {
            //过期时间
            Date expiration = toDate(localDateTime.plusSeconds(second));
            // 系统时间之前的token都是不可以被承认的
            builder.setExpiration(expiration).setNotBefore(now);
        }
        // 生成JWT
        return builder.compact();
    }

    /**
     * 验证令牌
     *
     * @param issuer         签发者
     * @param jsonWebToken   令牌
     * @param base64Security 解密的签名key : base64编码的字符串
     * @return
     */
    public static Claims parseJWT(String issuer, String jsonWebToken, String base64Security) {
        try {
            log.debug("parseJWT->>issuer:{},jsonWebToken:{},base64Security:{}", issuer, jsonWebToken, base64Security);
            return Jwts.parserBuilder()
                    .requireIssuer(issuer)
                    .setSigningKey(generalKey(base64Security))
                    .build().parseClaimsJws(jsonWebToken).getBody();
        } catch (Exception e) {
            log.warn("parseJWT error,token is expire");
            return null;
        }
    }

    /**
     * jwt过期时，重新生成新的 jwt
     *
     * @param token
     * @return
     */
    public static String updateJWT(String issuer, String token, String base64Security, int second) {
        try {
            Claims claims = parseJWT(issuer, token, base64Security);
            if (claims != null) {
                return createJWT(issuer, claims.getSubject(), base64Security, second);
            }
        } catch (Exception e) {
            log.error("updateJWT error.", e);
        }
        return null;
    }

    /**
     * 解析令牌,获取Subject，负载Payload使用的Base64Url编码，解码也要用Base64Url
     *
     * @param token 令牌
     * @return
     */
    public static String getSubject(String token) {
        try {
            if (StrUtil.isEmpty(token)) {
                return "";
            }
            String[] strs = token.split("\\.");
            String subject = strs[1];
            log.debug("subject:{}", subject);
            String payload = new String(Base64.getUrlDecoder().decode(subject), StandardCharsets.UTF_8);
            JSONObject jsonObject = JSONObject.parseObject(payload);
            return jsonObject.getString("sub");
        } catch (Exception e) {
            log.error("出错的token：{}", token, e);
            return "";
        }
    }

    /**
     * 由base64位的字符串生成加密key （私钥）
     *
     * @param base64Security 经过base64编码的密钥串
     * @return
     */
    public static Key generalKey(String base64Security) {
        //根据算法对key加密
        return Keys.hmacShaKeyFor(base64Security.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 随机生成一个base64编码的密钥字符串
     *
     * @return
     */
    public static String generalBase64Security() {
        //根据算法对key加密
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return Encoders.BASE64.encode(key.getEncoded());
    }

    /**
     * 由base64位的字符串生成加密key （私钥）
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    public static void main(String[] args) {
        /*String issuer = "chengzb";
        LoginContext loginContext = new LoginContext();
        loginContext.setUserId(1L);
        loginContext.setUserName("程昭斌");
        String user = JSON.toJSONString(loginContext);
        String base64Security = "UNIS7th5hTHw9IhSbWMDwC/01Cgd3vSmdoqeuAYybmc=";
        int exp = 86400*10;*/
        String base64Key = generalBase64Security();
      System.out.println("base64Key：" + base64Key);

      //  String token1 = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ3d3cuY2hlbmd6Yi5jb20iLCJpYXQiOjE2MDQ5ODc3MTIsInN1YiI6IntcInVzZXJJZFwiOjF9IiwiZXhwIjoxNjA3MTQ3ODkyLCJuYmYiOjE2MDQ5ODc3MTJ9.9cEoxQJDMYfYvEPXIlMi5rQqIevLAuOWS-txIugEvE0";
       /* String token = createJWT(issuer, user, base64Security, exp);
        System.out.println("token：" + token);
        Claims claims = parseJWT(issuer, token, base64Security);
        System.out.println("claims：" + claims);*/
    }
}
