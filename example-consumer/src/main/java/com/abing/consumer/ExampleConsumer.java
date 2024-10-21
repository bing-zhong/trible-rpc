package com.abing.consumer;

import com.abing.common.model.User;
import com.abing.common.service.UserService;
import com.abing.core.RpcApplication;
import com.abing.core.bootstrap.TribleBootstrap;
import com.abing.core.proxy.ServiceProxyFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @Author CaptainBing
 * @Date 2024/9/26 18:06
 * @Description
 */
@Slf4j
public class ExampleConsumer {

    public static void main(String[] args) {

        TribleBootstrap bootstrap = new TribleBootstrap();
        bootstrap.initConsumer();
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setId(1L);
        user.setName("abing");
        user.setAge(18);
        User resUser = userService.getUser(user);

        if (Objects.isNull(resUser)){
            log.info("用户不存在");
        }else {
            log.info("用户存在:" + resUser);
        }
        System.out.println();

    }

}
