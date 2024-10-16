package com.abing.core.protocol.codec;

import com.abing.core.protocol.ProtocolMessage;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 13:51
 * @Description
 */
public interface ProtocolMessageDecoder {

    /**
     * 解码
     * @param buffer
     * @return
     */
    ProtocolMessage<?> decode(Buffer buffer) throws IOException;


}
