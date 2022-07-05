package com.hezhan.shirodemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hezhan.shirodemo.entity.User;

public interface UserService extends IService<User> {

    User getUserByName(String userName);

    /**
     * 修改用户的状态
     * @param userId 用户id
     * @param state 状态
     */
    void updateUserState(Integer userId, Integer state);
}
