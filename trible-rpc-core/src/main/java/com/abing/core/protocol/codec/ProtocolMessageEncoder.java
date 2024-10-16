package com.abing.core.protocol.codec;

import com.abing.core.protocol.ProtocolMessage;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 13:50
 * @Description 协议编码器
 */
public interface ProtocolMessageEncoder {

    /**
     * 编码
     * @param protocolMessage
     * @return
     */
    Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException;


}
