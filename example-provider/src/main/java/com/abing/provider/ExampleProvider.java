package com.abing.provider;

import com.abing.common.service.UserService;
import com.abing.core.RpcApplication;
import com.abing.core.register.LocalRegistry;
import com.abing.core.server.http.VertxHttpServer;

/**
 * @Author CaptainBing
 * @Date 2024/9/26 18:04
 * @Description
 */
public class ExampleProvider {

    public static void main(String[] args) {
        RpcApplication.init();
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(8888);
    }

}
