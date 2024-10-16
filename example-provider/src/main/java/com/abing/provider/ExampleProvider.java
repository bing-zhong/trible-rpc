package com.abing.provider;

import com.abing.common.service.UserService;
import com.abing.core.RpcApplication;
import com.abing.core.config.RpcConfig;
import com.abing.core.model.registry.ServiceMetaInfo;
import com.abing.core.register.LocalRegistry;
import com.abing.core.registry.Registry;
import com.abing.core.registry.RegistryConfig;
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

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = Registry.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);

        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceName(UserService.class.getName());
        serviceMetaInfo1.setServiceHost("127.0.0.1");
        serviceMetaInfo1.setServicePort(rpcConfig.getPort());
        registry.register(serviceMetaInfo1);

        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(rpcConfig.getPort());
    }

}
