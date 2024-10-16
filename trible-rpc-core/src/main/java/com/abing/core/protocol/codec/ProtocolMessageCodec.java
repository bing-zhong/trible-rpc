package com.abing.core.protocol.codec;

import com.abing.core.model.api.RpcRequest;
import com.abing.core.model.api.RpcResponse;
import com.abing.core.protocol.ProtocolMessage;
import com.abing.core.protocol.ProtocolMessageTypeEnum;
import com.abing.core.protocol.constants.ProtocolConstant;
import com.abing.core.serialize.Serializer;
import com.abing.core.serialize.key.SerializerKeys;
import com.abing.core.spi.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;
import java.util.Objects;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 13:52
 * @Description 协议编解码器
 */
public class ProtocolMessageCodec implements ProtocolMessageEncoder, ProtocolMessageDecoder {


    @Override
    public ProtocolMessage<?> decode(Buffer buffer) throws IOException {

        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new RuntimeException("魔数非法");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        byte serializerType = buffer.getByte(2);
        header.setSerializer(serializerType);
        byte messageType = buffer.getByte(3);
        header.setMessageType(messageType);
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        int bodyLength = buffer.getInt(13);
        header.setBodyLength(bodyLength);
        byte[] bodyBytes = buffer.getBytes(17, bodyLength + 17);

        SerializerKeys serializerKey = SerializerKeys.of(serializerType);
        if (Objects.isNull(serializerKey)) {
            throw new EnumConstantNotPresentException(SerializerKeys.class, "序列化方式不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerKey.name());

        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.of(messageType);
        if (Objects.isNull(messageTypeEnum)) {
            throw new EnumConstantNotPresentException(ProtocolMessageTypeEnum.class, "消息类型不存在");
        }
        switch (messageTypeEnum) {
            case REQUEST:
                RpcRequest rpcRequest = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header,rpcRequest);
            case RESPONSE:
                RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header,rpcResponse);
            case HEARTBEAT:
            default:
                throw new RuntimeException("消息类型非法");
        }
    }

    @Override
    public Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {

        Buffer buffer = Buffer.buffer();
        if (Objects.isNull(protocolMessage) || Objects.isNull(protocolMessage.getHeader())) {
            return buffer;
        }

        ProtocolMessage.Header header = protocolMessage.getHeader();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getMessageType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        SerializerKeys serializerKey = SerializerKeys.of(header.getSerializer());
        if (Objects.isNull(serializerKey)) {
            throw new EnumConstantNotPresentException(SerializerKeys.class, "序列化方式不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerKey.name());
        Object body = protocolMessage.getBody();
        byte[] bodyBytes = serializer.serialize(body);
        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);
        return buffer;

    }
}
