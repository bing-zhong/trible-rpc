package com.abing.consumer;

import com.abing.common.model.User;
import com.abing.common.service.UserService;
import com.abing.core.config.RpcConfig;
import com.abing.core.proxy.ServiceProxyFactory;
import com.abing.core.utils.ConfigUtils;

import java.util.Objects;

/**
 * @Author CaptainBing
 * @Date 2024/9/26 18:06
 * @Description
 */
public class ExampleConsumer {

    public static void main(String[] args) {

        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class);
        System.out.println("rpcConfig = " + rpcConfig);
//        UserService userService = new UserServiceProxy();
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setId(1L);
        user.setName("abing");
        user.setAge(18);
        User resUser = userService.getUser(user);

        if (Objects.isNull(resUser)){
            System.out.println("用户不存在");
        }else {
            System.out.println("用户存在:" + resUser);
        }

    }

}
