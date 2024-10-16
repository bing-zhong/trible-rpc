package com.abing.core.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.abing.core.RpcApplication;
import com.abing.core.config.RpcConfig;
import com.abing.core.model.api.RpcRequest;
import com.abing.core.model.api.RpcResponse;
import com.abing.core.model.registry.ServiceMetaInfo;
import com.abing.core.registry.Registry;
import com.abing.core.registry.RegistryConfig;
import com.abing.core.serialize.Serializer;
import com.abing.core.spi.SpiFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/9/30 13:59
 * @Description
 */
public class ServiceProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                                          .serviceName(serviceName)
                                          .methodName(method.getName())
                                          .parameterTypes(method.getParameterTypes())
                                          .args(args)
                                          .build();

        try {
            Serializer serializer = SpiFactory.getInstance(RpcApplication.getRpcConfig().getSerialization().name());
            byte[] requestBytes = serializer.serialize(rpcRequest);

            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = Registry.getInstance(registryConfig.getRegistry());
            registry.init(registryConfig);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscover(serviceName);

            ServiceMetaInfo serviceMetaInfo = serviceMetaInfoList.get(0);

            HttpResponse httpResponse = HttpRequest.post(serviceMetaInfo.getServiceAddress())
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
