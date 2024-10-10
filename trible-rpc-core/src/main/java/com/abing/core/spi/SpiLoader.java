package com.abing.core.spi;

import com.abing.core.serialize.JdkSerializer;
import com.abing.core.serialize.Serializer;
import com.abing.core.serialize.key.SerializerKeys;

import java.util.Arrays;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author CaptainBing
 * @Date 2024/10/10 13:59
 * @Description
 */
public class SpiLoader {

    private static final Map<String, Class<?>> LOADER_MAP = new ConcurrentHashMap<>();

    public static <T> void load(Class<T> clazz) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
        serviceLoader.forEach(serializer -> {
            LOADER_MAP.put(getKey(serializer.getClass().getSimpleName()),serializer.getClass());
        });
    }

    public static Serializer getInstance(String key) {
        Class<?> serializerClass = LOADER_MAP.getOrDefault(key, JdkSerializer.class);
        try {
            return (Serializer) serializerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getKey(String key) {
        String suffix = "Serializer";
        return Arrays.stream(key.split(suffix))
                     .findFirst()
                     .map(String::toUpperCase)
                     .orElse(SerializerKeys.JDK.name());
    }

}
