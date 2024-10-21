package com.abing.core.bootstrap;

import com.abing.core.RpcApplication;
import com.abing.core.config.RpcConfig;
import com.abing.core.model.ServiceRegisterInfo;
import com.abing.core.model.registry.ServiceMetaInfo;
import com.abing.core.register.LocalRegistry;
import com.abing.core.registry.Registry;
import com.abing.core.registry.RegistryConfig;
import com.abing.core.server.tcp.VertxTcpServer;
import com.abing.core.spi.RegistryFactory;
import com.abing.core.utils.RandomPortFinder;

import java.io.IOException;
import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 17:01
 * @Description
 */
public class TribleBootstrap implements Bootstrap {
    @Override
    public void initProvider(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {

        // Rpc 框架初始化 (配置和注册中心)
        RpcApplication.init();

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistry();
        Registry registry = RegistryFactory.getInstance(registryConfig.getType().name());
        registry.init(registryConfig);
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            Class<?> serviceClass = serviceRegisterInfo.getServiceClass();
            LocalRegistry.register(serviceName, serviceClass);

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getHost());
            serviceMetaInfo.setServicePort(rpcConfig.getPort());
            registry.register(serviceMetaInfo);
        }

        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        int randomAvailablePort;
        try {
            randomAvailablePort = RandomPortFinder.getRandomAvailablePort();
        } catch (IOException e) {
            randomAvailablePort = rpcConfig.getPort();
        }
        vertxTcpServer.doStart(randomAvailablePort);
    }

    @Override
    public void initConsumer() {
        RpcApplication.init();
    }
}
