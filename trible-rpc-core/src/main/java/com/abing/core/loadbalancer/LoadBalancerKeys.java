package com.abing.core.loadbalancer;

import lombok.Getter;

/**
 * @Author CaptainBing
 * @Date 2024/10/17 10:21
 * @Description
 */
@Getter
public enum LoadBalancerKeys {

    /**
     * 随机
     */
    RANDOM,
    /**
     * 轮询
     */
    ROUNDROBIN,
    /**
     * 一致性hash
     */
    CONSISTENCEHASH;

}
