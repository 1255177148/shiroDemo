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
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Component
@DependsOn("myHashedCredentialsMatcher")
public class MyRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    /**
     * 构造器中配置登录校验器
     */
    public MyRealm(MyHashedCredentialsMatcher myHashedCredentialsMatcher) {
        super();
        myHashedCredentialsMatcher.setHashAlgorithmName("SHA-1");// 加密算法的名称
        myHashedCredentialsMatcher.setHashIterations(2);// 加密的次数
        myHashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);// 是否储存为16进制
        this.setCredentialsMatcher(myHashedCredentialsMatcher);
    }

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

    /**
     * <p>设置此Realm处理哪种类型的登录，这里标明处理UsernamePasswordToken类型的登录，也就是账号密码形式的登录。</p>
     * <p>因为shiro的机制是根据subject.login(token)这个登录方法中的token类型来分配Realm</p>
     * @return
     */
    @Override
    public Class getAuthenticationTokenClass() {
        return UsernamePasswordToken.class;
    }
}
