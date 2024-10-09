package com.abing.core.serialize;

import java.io.*;

/**
 * @Author CaptainBing
 * @Date 2024/9/27 9:43
 * @Description
 */
public class JdkSerializer implements Serializer{


    @Override
    public <T> byte[] serialize(T t) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(t);
        objectOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        try {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            objectInputStream.close();
        }
    }
}
