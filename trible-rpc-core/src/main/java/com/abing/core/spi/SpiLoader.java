package com.abing.core.spi;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author CaptainBing
 * @Date 2024/10/10 13:59
 * @Description
 */
public class SpiLoader {

    private static final Map<String, Map<String,Class<?>>> LOADER_MAP = new ConcurrentHashMap<>();
    private static final Map<String, Map<String,Object>> OBJECT_LOADER_MAP = new ConcurrentHashMap<>();

    public static <T> void load(Class<T> clazz) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
        Map<String, Class<?>> classMap = new HashMap<>();
        Map<String, Object> objectMap = new HashMap<>();
        serviceLoader.forEach(serializer -> {
            String serviceSimpleName = serializer.getClass().getSimpleName();
            String interfaceSimpleName = clazz.getSimpleName();
            classMap.put(serviceSimpleName.split(interfaceSimpleName)[0].toUpperCase(),serializer.getClass());
            objectMap.put(serviceSimpleName.split(interfaceSimpleName)[0].toUpperCase(),serializer);
        });
        LOADER_MAP.put(clazz.getName(), classMap);
        OBJECT_LOADER_MAP.put(clazz.getName(), objectMap);
    }

    public static <T> T getInstance(Class<T> target, String key) {
        Map<String, Object> objectMap = OBJECT_LOADER_MAP.get(target.getName());
        Object obj = objectMap.get(key);
        return (T) obj;
    }

}
