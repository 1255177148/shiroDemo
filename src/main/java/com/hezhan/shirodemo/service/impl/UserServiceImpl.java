package com.hezhan.shirodemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hezhan.shirodemo.entity.User;
import com.hezhan.shirodemo.mapper.UserMapper;
import com.hezhan.shirodemo.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUserByName(String userName) {
        LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>().lambda();
        wrapper.eq(User::getName, userName);
        return getOne(wrapper);
    }

    @Override
    public void updateUserState(Integer userId, Integer state) {
        LambdaUpdateWrapper<User> updateWrapper = new UpdateWrapper<User>().lambda();
        updateWrapper.set(User::getState, state);
        updateWrapper.eq(User::getId, userId);
        update(updateWrapper);
    }
}
