package com.abing.core.registry;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author CaptainBing
 * @Date 2024/10/10 15:46
 * @Description
 */
@Getter
@AllArgsConstructor
public enum RegistryKeys {

    /**
     * zookeeper注册中心
     */
    ZOOKEEPER(new ZookeeperRegistry()),
    /**
     * etcd注册中心
     */
    ETCD(new EtcdRegistry());

    private final Registry registry;

}
