package com.abing.core.spi;

import com.abing.core.loadbalancer.LoadBalancer;

/**
 * @Author CaptainBing
 * @Date 2024/10/17 10:03
 * @Description
 */
public class LoadBalancerFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }

    public static LoadBalancer getInstance(String key){
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }

}
