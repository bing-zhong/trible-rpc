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
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    public Object sendMessage(ProtocolMessage<?> protocolMessage) throws Exception{

        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        client.connect(address, response -> {
            if (response.succeeded()) {
                 NetSocket netSocket = response.result();
                Buffer requestBuffer = null;
                try {
                    requestBuffer = codec.encode(protocolMessage);
                } catch (IOException e) {
                    throw new RuntimeException("协议消息编码错误",e);
                }
                netSocket.write(requestBuffer);
                log.debug("send data: {}", HexUtil.encodeHexStr(requestBuffer.getBytes()));

                RecordParserWrapper recordParserWrapper = new RecordParserWrapper(buffer -> {
                    log.info("received data: {}", buffer.toString(StandardCharsets.UTF_8));
                    try {
                        ProtocolMessage<RpcResponse> decodeProtocolMessage = (ProtocolMessage<RpcResponse>) codec.decode(buffer);
                        responseFuture.complete(decodeProtocolMessage.getBody());
                    } catch (IOException e) {
                        throw new RuntimeException("协议消息解码错误",e);
                    }
                });
                netSocket.handler(recordParserWrapper);
            }else {
                log.error("failed to connect: {}", response.cause().getMessage());
            }
        });
        RpcResponse rpcResponse = null;
        try {
            rpcResponse = responseFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("获取响应结果失败",e);
        }
        client.close();
        return rpcResponse.getData();
    }

}
