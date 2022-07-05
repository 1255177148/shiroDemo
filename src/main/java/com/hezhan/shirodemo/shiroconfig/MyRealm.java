package com.hezhan.shirodemo.shiroconfig;

import com.hezhan.shirodemo.entity.User;
import com.hezhan.shirodemo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

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
        if (user == null || StringUtils.isBlank(userName) || StringUtils.isBlank(password)){
            throw new AuthenticationException();
        }
//        if (user.getState() == 1){
//            throw new ExcessiveAttemptsException();
//        }
        // 这里将user作为主体存放起来，后面要用的话，可以 (User) SecurityUtils.getSubject().getPrincipal(); 这样获取
        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getName()), getName());
    }
}
