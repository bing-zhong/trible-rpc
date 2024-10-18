package com.abing.core.server.tcp;

import cn.hutool.core.util.HexUtil;
import com.abing.core.model.api.RpcResponse;
import com.abing.core.protocol.ProtocolMessage;
import com.abing.core.protocol.codec.ProtocolMessageCodec;
import com.abing.core.protocol.wrapper.RecordParserWrapper;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 15:11
 * @Description
 */
@Slf4j
public class VertxTcpClient {

    private final NetClient client;
    private final SocketAddress address;
    private final ProtocolMessageCodec codec = new ProtocolMessageCodec();

    public VertxTcpClient(SocketAddress address) {
        Vertx vertx = Vertx.vertx();
        this.client = vertx.createNetClient();
        this.address = address;
    }

    /**
     * 发送消息
     * @param protocolMessage
     */
    public RpcResponse sendMessage(ProtocolMessage<?> protocolMessage) throws Exception{

        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        client.connect(address, response -> {
            if (response.succeeded()) {
                NetSocket netSocket = response.result();
                Buffer requestBuffer = null;
                try {
                    requestBuffer = codec.encode(protocolMessage);
                } catch (Exception e) {
                    responseFuture.completeExceptionally(new RuntimeException("protocol message encode error",e));
                    throw new RuntimeException("protocol message encode error",e);
                }
                netSocket.write(requestBuffer);
                log.debug("send data: {}", HexUtil.encodeHexStr(requestBuffer.getBytes()));

                RecordParserWrapper recordParserWrapper = new RecordParserWrapper(buffer -> {
                    try {
                        ProtocolMessage<RpcResponse> decodeProtocolMessage = (ProtocolMessage<RpcResponse>) codec.decode(buffer);
                        log.info("received data: {}", decodeProtocolMessage);
                        responseFuture.complete(decodeProtocolMessage.getBody());
                    } catch (Exception e) {
                        responseFuture.completeExceptionally(new RuntimeException("protocol message decode error",e));

                    }
                });
                netSocket.handler(recordParserWrapper);
            }else {
                String errorMessage = String.format("failed to connect: %s", response.cause().getMessage());
                log.error(errorMessage);
                responseFuture.completeExceptionally(new RuntimeException(errorMessage));
            }
        });
        RpcResponse rpcResponse = null;
        try {
            rpcResponse = responseFuture.get();
        } catch (Exception e) {
            throw new RuntimeException("receive response failed",e);
        }
        client.close();
        return rpcResponse;
    }

}
