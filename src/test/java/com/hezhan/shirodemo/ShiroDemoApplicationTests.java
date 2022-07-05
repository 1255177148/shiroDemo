package com.hezhan.shirodemo;

import com.hezhan.shirodemo.entity.User;
import com.hezhan.shirodemo.service.UserService;
import com.hezhan.shirodemo.util.EncryptionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ShiroDemoApplicationTests {

    @Resource
    private UserService userService;

    /**
     * 初始化一个账户
     */
    @Test
    void initUser() {
        User user = new User();
        user.setName("he");
        user.setPassword(EncryptionUtil.encryption("123456", "he"));
        user.setState(0);
        userService.save(user);
    }

}
