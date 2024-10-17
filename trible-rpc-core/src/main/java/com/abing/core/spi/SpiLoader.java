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

    public static <T> void load(Class<T> clazz) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
        Map<String, Class<?>> classMap = new HashMap<>();
        serviceLoader.forEach(serializer -> {
            String serviceSimpleName = serializer.getClass().getSimpleName();
            String interfaceSimpleName = clazz.getSimpleName();
            classMap.put(serviceSimpleName.split(interfaceSimpleName)[0].toUpperCase(),serializer.getClass());
        });
        LOADER_MAP.put(clazz.getName(), classMap);
    }

    public static <T> T getInstance(Class<T> target, String key) {
        Map<String, Class<?>> classMap = LOADER_MAP.get(target.getName());
        Class<?> serializerClass = classMap.get(key);
        try {
            return (T) serializerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
