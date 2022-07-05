package com.hezhan.shirodemo.handler;

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
}
