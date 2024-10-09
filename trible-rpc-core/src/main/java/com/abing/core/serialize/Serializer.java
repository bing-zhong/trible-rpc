package com.abing.core.serialize;

import java.io.IOException;

/**
 * @Author CaptainBing
 * @Date 2024/9/27 9:34
 * @Description
 */
public interface Serializer {

    /**
     * 序列化
     * @param t
     * @return
     */
    <T> byte[] serialize(T t) throws IOException;

    /**
     * 反序列化
     * @param bytes
     * @param type
     * @return
     */
    <T> T deserialize(byte[] bytes,Class<T> type) throws IOException;


}
