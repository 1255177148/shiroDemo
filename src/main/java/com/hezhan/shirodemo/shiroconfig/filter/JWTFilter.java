package com.hezhan.shirodemo.shiroconfig.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hezhan.shirodemo.exception.NotLoginException;
import com.hezhan.shirodemo.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.web.filter.authc.BearerHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JWTFilter extends BearerHttpAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        boolean res = false;
        HttpServletRequest req = WebUtils.toHttp(request);
        // 先判断是否传入了token
        if (!isLoginAttempt(request, response)){
            req.setAttribute("exception", new NotLoginException("未登录！"));
            req.getRequestDispatcher("/api/loginError").forward(request, response);
            return false;
        }
        // 再看是否过期
        BearerToken token = (BearerToken) createToken(request, response);
        if (JWTUtil.isExpire(token.getToken())){
            // 刷新token
            if (!refreshToken(request, response)){
                req.getRequestDispatcher("/api/loginExpire").forward(request, response);
                return false;
            }
            return true;
        }
        try {
            /*
            这里最终会调用subject.login(token)去处理认证，
            这里因为继承了BearerHttpAuthenticationFilter，
            所以这里会自动包装成一个BearerToken作为参数代入subject.login(token)中，
            怎么包装的呢？原来它会从请求头里获取一个"Authorization"字段的值，拿到这个值去进行包装
             */
            res = super.onAccessDenied(request, response);
        } catch (Exception e){
            Throwable cause = e.getCause();
            if (cause instanceof TokenExpiredException){
                refreshToken(request, response);
            } else {
                throw e;
            }
        }
        return res;
    }

    /**
     * 刷新token
     * @param request
     * @param response
     * @return
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response){
        log.info("刷新token...");
        HttpServletRequest req= (HttpServletRequest) request;
        String tokenHeader = req.getHeader(AUTHORIZATION_HEADER);
        String[] tokens = tokenHeader.split(" ");
        String token = tokens[1];
        String userName = JWTUtil.getUserInfo(token);
        String newToken = JWTUtil.createToken(userName);
        BearerToken bearerToken = new BearerToken(newToken);
        try {
            getSubject(request, response).login(bearerToken);
            HttpServletResponse res = (HttpServletResponse) response;
            res.setHeader("Access-Control-Expose-Headers", "Authorization");
            res.setHeader("Authorization", "Bearer " + newToken);
            return true;
        } catch (Exception e){
            log.error("token刷新失败", e);
            return false;
        }
    }
}
