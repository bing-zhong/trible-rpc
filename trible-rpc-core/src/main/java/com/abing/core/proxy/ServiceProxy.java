package com.abing.core.proxy;

import cn.hutool.core.util.IdUtil;
import com.abing.core.RpcApplication;
import com.abing.core.config.RpcConfig;
import com.abing.core.model.api.RpcRequest;
import com.abing.core.model.registry.ServiceMetaInfo;
import com.abing.core.protocol.ProtocolMessage;
import com.abing.core.protocol.ProtocolMessageStatusEnum;
import com.abing.core.protocol.ProtocolMessageTypeEnum;
import com.abing.core.protocol.constants.ProtocolConstant;
import com.abing.core.registry.Registry;
import com.abing.core.registry.RegistryConfig;
import com.abing.core.serialize.key.SerializerKeys;
import com.abing.core.server.tcp.VertxTcpClient;
import io.vertx.core.net.SocketAddress;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/9/30 13:59
 * @Description
 */
@Slf4j
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

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = Registry.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscover(serviceName);
        ServiceMetaInfo serviceMetaInfo = serviceMetaInfoList.get(0);

        ProtocolMessage<RpcRequest> rpcRequestProtocolMessage = getRpcRequestProtocolMessage(rpcRequest, rpcConfig);
        VertxTcpClient vertxTcpClient = new VertxTcpClient(SocketAddress.inetSocketAddress(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost()));
        return vertxTcpClient.sendMessage(rpcRequestProtocolMessage);

    }

    /**
     * 获取协议解码信息体
     * @param rpcRequest
     * @param rpcConfig
     * @return
     */
    private static ProtocolMessage<RpcRequest> getRpcRequestProtocolMessage(RpcRequest rpcRequest, RpcConfig rpcConfig) {
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        SerializerKeys serializerKey = rpcConfig.getSerialization();
        header.setSerializer((byte) serializerKey.getType());
        header.setMessageType((byte) ProtocolMessageTypeEnum.REQUEST.getCode());
        header.setStatus((byte) ProtocolMessageStatusEnum.SUCCESS.getCode());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        ProtocolMessage<RpcRequest> rpcRequestProtocolMessage = new ProtocolMessage<>(header, rpcRequest);
        return rpcRequestProtocolMessage;
    }
}
