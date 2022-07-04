package com.hezhan.shirodemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    public void updateUserState(String userName, Integer state) {
        LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>().lambda();
        wrapper.eq(User::getName, userName);
        User user = getOne(wrapper);
        if (user == null){
            return;
        }
        user.setState(state);
        updateById(user);
    }
}
