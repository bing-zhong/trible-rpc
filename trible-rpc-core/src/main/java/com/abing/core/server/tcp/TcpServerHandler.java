package com.abing.core.server.tcp;

import cn.hutool.core.util.HexUtil;
import com.abing.core.model.api.RpcRequest;
import com.abing.core.model.api.RpcResponse;
import com.abing.core.protocol.ProtocolMessage;
import com.abing.core.protocol.ProtocolMessageStatusEnum;
import com.abing.core.protocol.ProtocolMessageTypeEnum;
import com.abing.core.protocol.codec.ProtocolMessageCodec;
import com.abing.core.protocol.constants.ProtocolConstant;
import com.abing.core.protocol.wrapper.RecordParserWrapper;
import com.abing.core.register.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 11:58
 * @Description
 */
@Slf4j
public class TcpServerHandler implements Handler<NetSocket> {

    private final ProtocolMessageCodec codec = new ProtocolMessageCodec();
    @Override
    public void handle(NetSocket netSocket) {
        RecordParserWrapper recordParserWrapper = new RecordParserWrapper(buffer -> parseMessage(netSocket, buffer));
        netSocket.handler(recordParserWrapper);
    }

    /**
     * 解析请求信息
     * @param netSocket
     * @param buffer
     */
    private void parseMessage(NetSocket netSocket, Buffer buffer) {
        log.info("收到客户端消息：" + HexUtil.encodeHexStr(buffer.getBytes()));
        ProtocolMessage<RpcRequest> rpcRequestProtocolMessage = null;
        try {
            rpcRequestProtocolMessage = (ProtocolMessage<RpcRequest>) codec.decode(buffer);
        } catch (IOException e) {
            throw new RuntimeException("协议消息解码错误",e);
        }
        RpcRequest rpcRequest = rpcRequestProtocolMessage.getBody();
        String serviceName = rpcRequest.getServiceName();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

        RpcResponse rpcResponse = new RpcResponse();
        Class<?> implClass = LocalRegistry.get(serviceName);
        try {
            Method method = implClass.getMethod(methodName, parameterTypes);
            Object result = method.invoke(implClass.newInstance(), args);
            rpcResponse.setData(result);
            rpcResponse.setDataType(method.getReturnType());
            rpcResponse.setMessage(ProtocolMessageStatusEnum.SUCCESS.name());
        } catch (Exception e) {
            e.printStackTrace();
            rpcResponse.setMessage(ProtocolMessageStatusEnum.BAD_RESPONSE.name());
            rpcResponse.setException(e);
        }
        ProtocolMessage.Header header = buildProtocolHeader(rpcRequestProtocolMessage);

        ProtocolMessage<RpcResponse> encodeProtocolMessage = new ProtocolMessage<>(header, rpcResponse);
        Buffer resultBuffer = null;
        try {
            resultBuffer = codec.encode(encodeProtocolMessage);
            log.info("返回客户端消息：" + HexUtil.encodeHexStr(resultBuffer.getBytes()));
            netSocket.write(resultBuffer);
        } catch (IOException e) {
            throw new RuntimeException("协议编码错误",e);
        }
    }

    /**
     * 构建协议头
     * @param rpcRequestProtocolMessage
     * @return
     */
    private static ProtocolMessage.Header buildProtocolHeader(ProtocolMessage<RpcRequest> rpcRequestProtocolMessage) {
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer(rpcRequestProtocolMessage.getHeader().getSerializer());
        header.setMessageType((byte) ProtocolMessageTypeEnum.RESPONSE.getCode());
        header.setStatus((byte) ProtocolMessageStatusEnum.SUCCESS.getCode());
        header.setRequestId(rpcRequestProtocolMessage.getHeader().getRequestId());
        return header;
    }
}
