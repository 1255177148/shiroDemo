package com.hezhan.shirodemo.controller;

import com.hezhan.shirodemo.entity.User;
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
        return "未登录，跳首页去登录";
    }

    /**
     * 使用账号密码来登录，通过登录校验后，返回token
     * @param loginInfo 登录信息
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestBody LoginInfo loginInfo) {
        // 创建一个subject，是shiro的登录用户主体
        Subject subject = SecurityUtils.getSubject();
        // 认证提交前准备token
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername(loginInfo.getUserName());
        token.setPassword(loginInfo.getPassword().toCharArray());
        // 执行登录
        try {
            /*
            这里就会调用Realm去处理登录校验之类的事情，至于用哪个Realm，就看这里传入的token是哪个类的token，
            然后由接管不同类型token的Realm去处理
             */
            subject.login(token);
            User user = (User) subject.getPrincipal();

        } catch (LockedAccountException e){
            subject.logout();
            return "账号已被锁定，请联系管理员！";
        } catch (UnknownAccountException e){
            subject.logout();
            return "未知账号！";
        } catch (ExcessiveAttemptsException e){
            subject.logout();
            return "账号或密码错误次数过多！请5分钟后再登录！";
        } catch (IncorrectCredentialsException e){
            subject.logout();
            return "密码不正确！";
        } catch (AuthenticationException e){
            subject.logout();
            return "账号或密码不正确！";
        }
        if (subject.isAuthenticated()){
            return "登录成功";
        } else {
            subject.logout();
            return "登录失败";
        }
    }

    @PostMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "退出登录！";
    }
}
