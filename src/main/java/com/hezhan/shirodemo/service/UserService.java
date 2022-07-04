package com.hezhan.shirodemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hezhan.shirodemo.entity.User;

public interface UserService extends IService<User> {

    User getUserByName(String userName);

    /**
     * 修改用户的状态
     * @param userName 用户名
     * @param state 状态
     */
    void updateUserState(String userName, Integer state);
}
