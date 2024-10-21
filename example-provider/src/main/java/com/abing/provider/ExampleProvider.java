package com.abing.provider;

import com.abing.common.service.UserService;
import com.abing.core.RpcApplication;
import com.abing.core.bootstrap.TribleBootstrap;
import com.abing.core.config.RpcConfig;
import com.abing.core.model.ServiceRegisterInfo;
import com.abing.core.model.registry.ServiceMetaInfo;
import com.abing.core.register.LocalRegistry;
import com.abing.core.registry.Registry;
import com.abing.core.registry.RegistryConfig;
import com.abing.core.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/9/26 18:04
 * @Description
 */
public class ExampleProvider {

    public static void main(String[] args) {

        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        serviceRegisterInfoList.add(new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class));
        TribleBootstrap bootstrap = new TribleBootstrap();
        bootstrap.initProvider(serviceRegisterInfoList);

    }

}
