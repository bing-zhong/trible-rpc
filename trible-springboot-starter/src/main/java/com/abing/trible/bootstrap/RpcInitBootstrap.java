package com.abing.trible.bootstrap;

import com.abing.core.RpcApplication;
import com.abing.core.config.RpcConfig;
import com.abing.core.server.tcp.VertxTcpServer;
import com.abing.core.utils.RandomPortFinder;
import com.abing.trible.annotation.EnableTrible;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 17:49
 * @Description
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableTrible.class.getName())
                                                             .get("needServer");
        RpcApplication.init();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        if (needServer) {
            // 启动服务端
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            int randomAvailablePort;
            try {
                randomAvailablePort = RandomPortFinder.getRandomAvailablePort();
            } catch (IOException e) {
                randomAvailablePort = rpcConfig.getPort();
            }
            vertxTcpServer.doStart(randomAvailablePort);
        }else {
            log.info("trible rpc 服务端未启动");
        }
    }
}
