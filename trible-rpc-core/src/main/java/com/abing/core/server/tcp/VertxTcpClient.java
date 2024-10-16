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

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 15:11
 * @Description
 */
@Slf4j
public class VertxTcpClient {

    private final NetClient client;
    private NetSocket netSocket;

    private final ProtocolMessageCodec codec = new ProtocolMessageCodec();

    public VertxTcpClient(SocketAddress address) {
        Vertx vertx = Vertx.vertx();
        this.client = vertx.createNetClient().connect(address, response -> {
            if (response.succeeded()) {
                this.netSocket = response.result();
            }else {
                log.info("Failed to connect: {}", response.cause().getMessage());
            }
        });
    }

    /**
     * 发送消息
     * @param protocolMessage
     */
    public Object sendMessage(ProtocolMessage<?> protocolMessage){

        Buffer requestBuffer = null;
        try {
            requestBuffer = codec.encode(protocolMessage);
        } catch (IOException e) {
            throw new RuntimeException("协议消息编码错误",e);
        }
        log.info("Send data: {}", HexUtil.encodeHexStr(requestBuffer.getBytes()));
        netSocket.write(requestBuffer);

        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        RecordParserWrapper recordParserWrapper = new RecordParserWrapper(buffer -> {
            log.info("Received data: {}", buffer.toString());
            try {
                ProtocolMessage<RpcResponse> decodeProtocolMessage = (ProtocolMessage<RpcResponse>) codec.decode(buffer);
                responseFuture.complete(decodeProtocolMessage.getBody());
            } catch (IOException e) {
                throw new RuntimeException("协议消息解码错误",e);
            }
        });
        netSocket.handler(recordParserWrapper);
        RpcResponse rpcResponse = responseFuture.join();
        client.close();
        return rpcResponse.getData();
    }

}
