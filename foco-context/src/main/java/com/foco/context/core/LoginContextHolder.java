package com.foco.context.core;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.foco.context.util.HttpContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


/**
 * description: 通过ThreadLocal 存储LoginContext
 *
 * @Author lucoo
 * @Date 2021/6/24 11:16
 */
@Slf4j
@Getter
@Setter
public class LoginContextHolder {

    private static ThreadLocal<String> THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void set(LoginContext loginContext) {
        set(JSON.toJSONString(loginContext));
    }
    public static void set(String loginContext) {
        THREAD_LOCAL.set(UnicodeUtil.toUnicode(loginContext));
    }
    public static String get() {
        return get(false);
    }
    public static String get(Boolean direct) {
        if(direct){
            return THREAD_LOCAL.get();
        }
        return UnicodeUtil.toString(THREAD_LOCAL.get());
    }
    public static void remove() {
        //防止内存泄漏
        THREAD_LOCAL.remove();
        if(log.isDebugEnabled()){
            log.debug(Thread.currentThread().getName() + " LoginContextHolder.remove {}");
        }
    }
    /**
     * 获取当前LoginContext 对象
     */
    public static <T extends LoginContext>  T  getLoginContext(Class<T> tClass){
        String loginContext = Optional.ofNullable(getLoginContext())
                .orElse(null);
        loginContext=UnicodeUtil.toString(loginContext);
        log.debug("loginContext={}", loginContext);
        return StrUtil.isBlank(loginContext) ?null: JSON.parseObject(loginContext,tClass);
    }
    private static String getLoginContext(){
        String context = get();
        if(StrUtil.isBlank(context)){
            LoginContextUpdater loginContextUpdater = SpringContextHolder.getBean(LoginContextUpdater.class);
            if(loginContextUpdater!=null){
                loginContextUpdater.update();
                context = get();
            }
        }
        return context;
    }
    public static String currentUserId(){
        LoginContext loginContext = getLoginContext(LoginContext.class);
        return loginContext!=null?loginContext.getUserId():null;
    }
    public static Long currentTenantId(){
        LoginContext loginContext = getLoginContext(LoginContext.class);
        return loginContext!=null?loginContext.getTenantId():null;
    }
    public static String currentUserName(){
        LoginContext loginContext = getLoginContext(LoginContext.class);
        return loginContext!=null?loginContext.getUserName():null;
    }
    public static void setUserName(String userName){
        LoginContext loginContext = getLoginContext(LoginContext.class);
        if(loginContext!=null){
            loginContext.setUserName(userName);
        }
    }
    public static void setTenantId(Long tenantId){
        LoginContext loginContext = getLoginContext(LoginContext.class);
        if(loginContext!=null){
            loginContext.setTenantId(tenantId);
        }
    }
    public static void setUserId(String userId) {
        LoginContext loginContext = getLoginContext(LoginContext.class);
        if(loginContext!=null){
            loginContext.setUserId(userId);
        }
    }
}
