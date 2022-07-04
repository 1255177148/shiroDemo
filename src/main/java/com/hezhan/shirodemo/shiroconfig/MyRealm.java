package com.hezhan.shirodemo.shiroconfig;

import com.hezhan.shirodemo.entity.User;
import com.hezhan.shirodemo.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

public class MyRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    /**
     * 授权，权限校验
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证，登录认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String userName = usernamePasswordToken.getUsername();
        String password = new String(usernamePasswordToken.getPassword());

        User user = userService.getUserByName(userName);
        if (user == null){
            throw new UnknownAccountException("用户名或密码错误！");
        }
        if (user.getState() == 1){
            throw new LockedAccountException("账号已被锁定，请联系管理员！");
        }
        return new SimpleAuthenticationInfo(user, password, ByteSource.Util.bytes(user.getName()), getName());
    }
}
