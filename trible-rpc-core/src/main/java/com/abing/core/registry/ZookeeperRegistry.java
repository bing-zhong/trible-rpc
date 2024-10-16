package com.abing.core.registry;

import com.abing.core.model.registry.ServiceMetaInfo;

import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/10/15 10:04
 * @Description
 */
public class ZookeeperRegistry implements Registry{


    @Override
    public void init(RegistryConfig registryConfig) {

    }

    @Override
    public void heartbeat() {

    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) {

    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {

    }

    @Override
    public List<ServiceMetaInfo> serviceDiscover(String serviceName) {
        return null;
    }

    @Override
    public void destroy() {

    }
}
