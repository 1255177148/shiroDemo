package com.hezhan.shirodemo.handler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hezhan.shirodemo.exception.NotLoginException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionGlobalHandler {

    @ExceptionHandler(value = UnauthorizedException.class)
    public String unauthorized(){
        return "无权限！";
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public String errorAuthorization(){
        return "权限认证失败！";
    }

    @ExceptionHandler(value = NotLoginException.class)
    public String notLogin(NotLoginException e){
        System.out.println(e.getMessage());
        return "未登录";
    }
}
