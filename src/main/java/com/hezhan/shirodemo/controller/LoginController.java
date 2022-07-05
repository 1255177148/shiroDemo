package com.hezhan.shirodemo.controller;

import com.hezhan.shirodemo.model.LoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class LoginController {

    @GetMapping("/login")
    public String frontPage(){
        return "首页";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginInfo loginInfo) throws Exception{
        // 创建一个subject，是shiro的登录用户主体
        Subject subject = SecurityUtils.getSubject();
        // 认证提交前准备token
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername(loginInfo.getUserName());
        token.setPassword(loginInfo.getPassword().toCharArray());
        // 执行登录
        try {
            subject.login(token);
        } catch (LockedAccountException e){
            return "账号已被锁定，请联系管理员！";
        } catch (UnknownAccountException e){
            return "未知账号！";
        } catch (ExcessiveAttemptsException e){
            return "账号或密码错误次数过多！请5分钟后再登录！";
        } catch (IncorrectCredentialsException e){
            return "密码不正确！";
        } catch (AuthenticationException e){
            return "账号或密码不正确！";
        }
        if (subject.isAuthenticated()){
            return "登录成功";
        } else {
            return "登录失败";
        }
    }
}
