package com.foco.auth.provider.db;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.foco.auth.common.AbstractTokenManager;
import com.foco.auth.provider.db.mapper.TokenInfoMapper;
import com.foco.auth.properties.TokenProperties;
import com.foco.properties.SystemConfig;
import com.foco.context.core.DataSourceContextHolder;
import com.foco.context.core.GenericLoginContext;
import com.foco.context.core.LoginContext;
import com.foco.context.core.LoginContextHolder;
import com.foco.context.util.BeanCopierEx;
import com.foco.context.util.HttpContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * @Description TODO
 * @Author lucoo
 * @Date 2021/6/3 9:36
 **/
@Slf4j
public class DbTokenManager extends AbstractTokenManager {
    @Autowired
    private TokenInfoMapper tokenInfoMapper;

    @Override
    public <T extends LoginContext> T parseToken(String key, Class<T> loginContext) {
        String[] split = key.split(":");
        Integer userId = Integer.valueOf(split[0]);
        String clientType = split[1];
        String token = split[2];
        LambdaQueryWrapper<TokenInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(TokenInfo::getUserId, userId)
                .eq(TokenInfo::getClientType, clientType)
                .eq(TokenInfo::getToken, token).select(TokenInfo::getUserInfo, TokenInfo::getExpireTime);
        TokenInfo tokenInfo = DataSourceContextHolder.switchMasterTemporary(() -> tokenInfoMapper.selectOne(lambdaQueryWrapper));
        if (tokenInfo == null) {
            log.warn("用户登录token不存在,userId:{}", userId);
            return null;
        }
        if (tokenInfo.getExpireTime().isBefore(LocalDateTime.now())) {
            //已过期
            deleteToken(key);
            log.warn("用户登录信息已过期,userId:{}", userId);
            return null;
        }

        String userInfo = tokenInfo.getUserInfo();
        return JSONObject.parseObject(userInfo, loginContext);
    }

    @Override
    public void refreshToken(String key, GenericLoginContext loginContext) {
        String[] split = key.split(":");
        Integer userId = Integer.valueOf(split[0]);
        String clientType = split[1];

        LambdaQueryWrapper<TokenInfo> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(TokenInfo::getUserId, userId)
                .eq(TokenInfo::getClientType, clientType);

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setExpireTime(LocalDateTime.now().plusSeconds(loginContext.getExpireTimeInSecond()));
        tokenInfoMapper.update(tokenInfo, lambdaQueryWrapper);
    }

    private void deleteToken(String token) {
        if (!token.contains(SEPARATOR)) {
            token = decrypt(token);
        }
        String[] split = token.split(SEPARATOR);
        Integer userId = Integer.valueOf(split[0]);
        String clientType = split[1];
        token = split[2];
        LambdaQueryWrapper<TokenInfo> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(TokenInfo::getUserId, userId)
                .eq(TokenInfo::getClientType, clientType)
                .eq(TokenInfo::getToken, token);
        tokenInfoMapper.delete(lambdaQueryWrapper);
    }

    @Override
    public void saveToken(GenericLoginContext loginContext, String token) {
        //删除同一客户端的用户登录信息
        LambdaQueryWrapper<TokenInfo> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(TokenInfo::getUserId, loginContext.getUserId())
                .eq(TokenInfo::getClientType, loginContext.getClientType());
        tokenInfoMapper.delete(lambdaQueryWrapper);

        TokenInfo tokenInfo = BeanCopierEx.copyProperties(loginContext, TokenInfo.class);
        tokenInfo.setToken(token);
        tokenInfo.setUserInfo(JSONObject.toJSONString(loginContext));
        tokenInfo.setExpireTime(LocalDateTime.now().plusSeconds(loginContext.getExpireTimeInSecond()));
        //存储token
        tokenInfoMapper.insert(tokenInfo);
    }

    @Override
    public boolean updateToken(GenericLoginContext loginContext) {
        LambdaQueryWrapper<TokenInfo> lambdaQueryWrapper = buildQuery();
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserInfo(JSON.toJSONString(loginContext));
        return tokenInfoMapper.update(tokenInfo, lambdaQueryWrapper) > 0;
    }

    @Override
    public boolean removeToken() {
        return tokenInfoMapper.delete(buildQuery()) > 0;
    }

    private LambdaQueryWrapper<TokenInfo> buildQuery() {
        String userId = LoginContextHolder.currentUserId();
        String clientType = HttpContext.getHeader(TokenProperties.getConfig().getClientTypeHead());
        if (StrUtil.isBlank(clientType)) {
            clientType = "none";
        }
        String token = HttpContext.getHeader(SystemConfig.getConfig().getTokenHead());
        LambdaQueryWrapper<TokenInfo> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(TokenInfo::getUserId, userId)
                .eq(TokenInfo::getClientType, clientType)
                .eq(TokenInfo::getToken, token);
        return lambdaQueryWrapper;
    }
}
