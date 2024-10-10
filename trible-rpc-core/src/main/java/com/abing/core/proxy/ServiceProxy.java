package com.abing.core.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.abing.core.RpcApplication;
import com.abing.core.model.api.RpcRequest;
import com.abing.core.model.api.RpcResponse;
import com.abing.core.serialize.Serializer;
import com.abing.core.spi.SpiFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author CaptainBing
 * @Date 2024/9/30 13:59
 * @Description
 */
public class ServiceProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcRequest rpcRequest = RpcRequest.builder()
                                          .serviceName(method.getDeclaringClass().getName())
                                          .methodName(method.getName())
                                          .parameterTypes(method.getParameterTypes())
                                          .args(args)
                                          .build();

        try {
            Serializer serializer = SpiFactory.getInstance(RpcApplication.getRpcConfig().getSerialization().name());
            byte[] requestBytes = serializer.serialize(rpcRequest);
            HttpResponse httpResponse = HttpRequest.post("http://localhost:8888")
                                                   .body(requestBytes)
                                                   .execute();
            byte[] bodyBytes = httpResponse.bodyBytes();
            RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
            return rpcResponse.getData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
