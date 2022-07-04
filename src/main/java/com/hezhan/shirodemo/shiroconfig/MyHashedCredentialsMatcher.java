package com.hezhan.shirodemo.shiroconfig;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hezhan.shirodemo.entity.User;
import com.hezhan.shirodemo.service.UserService;
import com.hezhan.shirodemo.util.RedisUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MyHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserService userService;

    public static final String KEY_PREFIX = "shiro:cache:retryLimit:";

    public static final Integer MAX = 5;// 最大登录次数

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 获取用户名
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String userName = usernamePasswordToken.getUsername();
        String key = KEY_PREFIX + userName;
        // 获取用户登录失败次数
        AtomicInteger atomicInteger = (AtomicInteger) redisUtil.get(key);
        if (atomicInteger == null){
            atomicInteger = new AtomicInteger(0);
        }
        if (atomicInteger.incrementAndGet() > MAX){
            // 如果用户登录失败次数大于5次，抛出锁定用户异常，并修改数据库用户状态字段
            User user = userService.getUserByName(userName);
            if (user != null){
                user.setState(1);// 设置为锁定状态
                userService.updateById(user);
                log.info("锁定用户"+ userName);
                throw new LockedAccountException();
            }
        }
        // 判断用户的账号和密码是否正确
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches){
            // 如果匹配上了
            redisUtil.delete(key);
            // 将用户的状态改为0
            userService.updateUserState(userName, 0);
        } else {
            redisUtil.set(key, atomicInteger);
        }
        return matches;
    }
}
