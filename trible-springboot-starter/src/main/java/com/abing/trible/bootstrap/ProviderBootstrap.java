package com.abing.trible.bootstrap;

import com.abing.core.model.registry.ServiceMetaInfo;
import com.abing.core.register.LocalRegistry;
import com.abing.core.registry.Registry;
import com.abing.core.registry.RegistryConfig;
import com.abing.core.spi.RegistryFactory;
import com.abing.core.utils.RandomPortFinder;
import com.abing.trible.annotation.TribleService;
import com.abing.trible.config.RpcConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 17:59
 * @Description
 */
public class ProviderBootstrap implements BeanPostProcessor {

    private final RpcConfiguration rpcConfiguration;

    @Lazy
    public ProviderBootstrap(RpcConfiguration rpcConfiguration) {
        this.rpcConfiguration = rpcConfiguration;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(TribleService.class)) {
            TribleService tribleService = beanClass.getAnnotation(TribleService.class);
            Class<?> interfaceClass = tribleService.interfaceClass();
            if (interfaceClass == void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            LocalRegistry.register(serviceName, beanClass);

            RegistryConfig registryConfig = rpcConfiguration.getRegistry();
            Registry registry = RegistryFactory.getInstance(registryConfig.getType().name());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceVersion(rpcConfiguration.getVersion());
            serviceMetaInfo.setServiceName(serviceName);
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                throw new RuntimeException("receive ip address failed", e);
            }
            String ipAddress = inetAddress.getHostAddress();
            serviceMetaInfo.setServiceHost(ipAddress);
            int randomAvailablePort;
            try {
                randomAvailablePort = RandomPortFinder.getRandomAvailablePort();
            } catch (IOException e) {
                throw new RuntimeException("tcp server port gen failed", e);
            }
            serviceMetaInfo.setServicePort(randomAvailablePort);
            registry.register(serviceMetaInfo);
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
