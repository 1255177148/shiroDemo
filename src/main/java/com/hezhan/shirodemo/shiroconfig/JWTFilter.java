package com.hezhan.shirodemo.shiroconfig;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hezhan.shirodemo.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.web.filter.authc.BearerHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JWTFilter extends BearerHttpAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        boolean res = false;
        try {
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
    private boolean refreshToken(ServletRequest request, ServletResponse response) throws Exception{
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
