package com.abing.core.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author CaptainBing
 * @Date 2024/9/30 9:39
 * @Description
 */
public class LocalRegistry {

    private static final Map<String,Class<?>> serviceMap = new ConcurrentHashMap<>();


    public static void register(String serviceName,Class<?> serviceClass){
        serviceMap.put(serviceName,serviceClass);
    }

    public static Class<?> get(String serviceName){
        return serviceMap.get(serviceName);
    }

    public static void remove(String serviceName){
        serviceMap.remove(serviceName);
    }


}
