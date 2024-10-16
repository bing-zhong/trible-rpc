package com.abing.core.registry;

import com.abing.core.model.registry.ServiceMetaInfo;

import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 10:10
 * @Description 注册中心服务缓存
 */
public class RegistryServiceCache {

    private List<ServiceMetaInfo> registryServiceCache;

    public List<ServiceMetaInfo> readCache() {
        return registryServiceCache;
    }

    public void writeCache(List<ServiceMetaInfo> registryServiceCache) {
        this.registryServiceCache = registryServiceCache;
    }

    public void clearCache() {
        this.registryServiceCache = null;
    }

}
