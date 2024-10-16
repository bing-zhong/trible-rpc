package com.abing.core.registry;

import com.abing.core.model.registry.ServiceMetaInfo;

import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/10/10 15:49
 * @Description
 */
public interface Registry {

    /**
     * 初始化注册中心
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 心跳检测
     */
    void heartbeat();

    /**
     * 监听服务
     */
    void watch(String serviceNodeKey);

    /**
     * 注册服务
     * @param serviceMetaInfo
     */
    void register(ServiceMetaInfo serviceMetaInfo);

    /**
     * 取消注册服务
     * @param serviceNodeKey
     */
    void unRegister(String serviceNodeKey);

    /**
     * 服务发现
     * @param serviceName
     * @return
     */
    List<ServiceMetaInfo> serviceDiscover(String serviceName);

    /**
     * 销毁注册中心
     */
    void destroy();

    /**
     * 获取注册中心
     * @param registryKeys
     * @return
     */
    static Registry getInstance(RegistryKeys registryKeys) {
        for (RegistryKeys key : RegistryKeys.values()) {
            if (key.equals(registryKeys)){
                return key.getRegistry();
            }
        }
        throw new EnumConstantNotPresentException(RegistryKeys.class, "不支持的注册中心类型");
    }

}
