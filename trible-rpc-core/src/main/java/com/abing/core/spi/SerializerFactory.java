package com.abing.core.spi;

import com.abing.core.serialize.Serializer;


/**
 * @Author CaptainBing
 * @Date 2024/10/10 14:16
 * @Description
 */
public class SerializerFactory {

    static {
        SerializerLoader.load(Serializer.class);
    }

    public static Serializer getInstance(String key){
        return SerializerLoader.getInstance(key);
    }

}
