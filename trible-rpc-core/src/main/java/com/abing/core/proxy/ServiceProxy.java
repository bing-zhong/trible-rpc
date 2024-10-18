package com.abing.core.proxy;

import cn.hutool.core.util.IdUtil;
import com.abing.core.RpcApplication;
import com.abing.core.config.RpcConfig;
import com.abing.core.loadbalancer.LoadBalancer;
import com.abing.core.loadbalancer.LoadBalancerKeys;
import com.abing.core.model.api.RpcRequest;
import com.abing.core.model.api.RpcResponse;
import com.abing.core.model.registry.ServiceMetaInfo;
import com.abing.core.protocol.ProtocolMessage;
import com.abing.core.protocol.ProtocolMessageStatusEnum;
import com.abing.core.protocol.ProtocolMessageTypeEnum;
import com.abing.core.protocol.constants.ProtocolConstant;
import com.abing.core.registry.Registry;
import com.abing.core.registry.RegistryConfig;
import com.abing.core.retry.RetryKeys;
import com.abing.core.retry.RetryStrategy;
import com.abing.core.serialize.key.SerializerKeys;
import com.abing.core.server.tcp.VertxTcpClient;
import com.abing.core.spi.LoadBalancerFactory;
import com.abing.core.spi.RetryFactory;
import io.vertx.core.net.SocketAddress;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author CaptainBing
 * @Date 2024/9/30 13:59
 * @Description
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {

    RpcConfig rpcConfig = RpcApplication.getRpcConfig();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {

        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                                          .serviceName(serviceName)
                                          .methodName(method.getName())
                                          .parameterTypes(method.getParameterTypes())
                                          .args(args)
                                          .build();

        // 注册中心
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = Registry.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);

        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscover(serviceName);
        if (serviceMetaInfoList.isEmpty()) {
            throw new RuntimeException("no service found for " + serviceName);
        }
        // 负载均衡
        LoadBalancerKeys balancerKey = rpcConfig.getBalancer();
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(balancerKey.name());
        // 将请求方法名作为负载均衡参数
        Map<String, Object> requestParam = new HashMap<>(1);
        requestParam.put("methodName",rpcRequest.getMethodName());
        ServiceMetaInfo serviceMetaInfo = loadBalancer.select(requestParam, serviceMetaInfoList);

        ProtocolMessage<RpcRequest> rpcRequestProtocolMessage = getRpcRequestProtocolMessage(rpcRequest, rpcConfig);
        SocketAddress socketAddress = SocketAddress.inetSocketAddress(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost());
        VertxTcpClient vertxTcpClient = new VertxTcpClient(socketAddress);

        // 重试策略
        RetryKeys type = rpcConfig.getRetry().getType();
        RetryStrategy retryStrategy = RetryFactory.getInstance(type.name());
        RpcResponse rpcResponse = retryStrategy.doRetry(() -> vertxTcpClient.sendMessage(rpcRequestProtocolMessage));
        return rpcResponse.getData();

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
