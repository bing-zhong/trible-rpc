package com.abing.core.registry;

import lombok.Data;

/**
 * @Author CaptainBing
 * @Date 2024/10/10 15:43
 * @Description
 */
@Data
public class RegistryConfig {

    /**
     * 注册中心类型
     */
    private RegistryKeys registry = RegistryKeys.ETCD;
    /**
     * 注册中心地址
     */
    private String address = "http://127.0.0.1:2380";
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 超时时间
     */
    private Long timeout = 10000L;

}
