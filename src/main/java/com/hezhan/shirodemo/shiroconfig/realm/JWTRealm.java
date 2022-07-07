package com.hezhan.shirodemo.shiroconfig.realm;

import com.hezhan.shirodemo.entity.User;
import com.hezhan.shirodemo.shiroconfig.matcher.MyJWTCredentialsMatcher;
import com.hezhan.shirodemo.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@DependsOn("myJWTCredentialsMatcher")
public class JWTRealm extends AuthorizingRealm {

    public JWTRealm(MyJWTCredentialsMatcher myJWTCredentialsMatcher) {
        super(myJWTCredentialsMatcher);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        // 这里获取到了登录信息后，可以根据用户从数据库里获取该用户所拥有的权限
        // 这里只作为演示，所以就写死了几个权限存放
        Set<String> permissions = new HashSet<>();
        permissions.add("user:show");
        permissions.add("user:admin");
        permissions.add("user:add");
        info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        BearerToken bearerToken = (BearerToken) token;
        String tokenString = bearerToken.getToken();
        String userName = JWTUtil.getUserInfo(tokenString);
        User user = new User();
        user.setName(userName);
        return new SimpleAuthenticationInfo(user, tokenString, getName());
    }

    @Override
    public Class getAuthenticationTokenClass() {
        return BearerToken.class;
    }
}
