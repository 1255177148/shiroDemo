package com.hezhan.shirodemo.shiroconfig.matcher;

import com.hezhan.shirodemo.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyJWTCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws RuntimeException{
        BearerToken bearerToken = (BearerToken) token;
        String tokenString = bearerToken.getToken();
        return JWTUtil.verifyToken(tokenString);
    }
}
