package com.abing.core.protocol.codec;

import cn.hutool.core.util.IdUtil;
import com.abing.core.constant.RpcConstant;
import com.abing.core.model.api.RpcRequest;
import com.abing.core.protocol.ProtocolMessage;
import com.abing.core.protocol.ProtocolMessageTypeEnum;
import com.abing.core.protocol.constants.ProtocolConstant;
import com.abing.core.serialize.key.SerializerKeys;
import io.vertx.core.buffer.Buffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 14:38
 * @Description
 */
class ProtocolMessageCodecTest {

    @Test
    void decode() throws IOException {

        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte) SerializerKeys.HESSIAN.getType());
        header.setMessageType((byte) ProtocolMessageTypeEnum.REQUEST.getCode());
        header.setRequestId(IdUtil.getSnowflakeNextId());

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("com.abing.core.service.UserService");
        rpcRequest.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setMethodName("myMethod");
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"abc"});

        ProtocolMessageCodec protocolMessageCodec = new ProtocolMessageCodec();
        Buffer buffer = protocolMessageCodec.encode(new ProtocolMessage<>(header, rpcRequest));
        ProtocolMessage<?> protocolMessage = protocolMessageCodec.decode(buffer);
        Assertions.assertNotNull(protocolMessage);

    }

    @Test
    void encode() {
    }
}