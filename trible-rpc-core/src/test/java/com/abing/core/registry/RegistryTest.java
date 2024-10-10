package com.abing.core.registry;

import com.abing.core.model.registry.ServiceMetaInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/10/10 18:12
 * @Description
 */
class RegistryTest {

    private static Registry registry = new EtcdRegistry();


    @BeforeAll
    static void init() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://localhost:2379");
        registry.init(registryConfig);
    }

    @Test
    void testRegister() {
        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceName("com.abing.rpc.service.UserService");
        serviceMetaInfo1.setServiceHost("127.0.0.1");
        serviceMetaInfo1.setServicePort(8081);
        registry.register(serviceMetaInfo1);
        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName("com.abing.rpc.service.UserService");
        serviceMetaInfo2.setServiceHost("127.0.0.1");
        serviceMetaInfo2.setServicePort(8082);
        registry.register(serviceMetaInfo2);
        ServiceMetaInfo serviceMetaInfo3 = new ServiceMetaInfo();
        serviceMetaInfo3.setServiceName("com.abing.rpc.service.UserService");
        serviceMetaInfo3.setServiceVersion("2.0.0");
        serviceMetaInfo3.setServiceHost("127.0.0.1");
        serviceMetaInfo3.setServicePort(8083);
        registry.register(serviceMetaInfo3);
    }

    @AfterAll
    static void testServiceDiscovery() {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("com.abing.rpc.service.UserService");
        List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscover(serviceMetaInfo.getServiceByPrefix());
        Assertions.assertEquals(2,serviceMetaInfos.size());
    }


}
