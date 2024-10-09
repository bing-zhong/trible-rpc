package com.abing.provider;

import com.abing.common.model.User;
import com.abing.common.service.UserService;

/**
 * @Author CaptainBing
 * @Date 2024/9/30 13:46
 * @Description
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user){
        System.out.println("收到请求：" + user);
        return user;
    }
}
