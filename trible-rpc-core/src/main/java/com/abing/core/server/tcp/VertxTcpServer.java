package com.abing.core.server.tcp;

import com.abing.core.server.Network;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 11:49
 * @Description
 */
@Slf4j
public class VertxTcpServer implements Network {


    @Override
    public void doStart(int port) {

        Vertx vertx = Vertx.vertx();
        NetServer netServer = vertx.createNetServer();
        netServer.connectHandler(new TcpServerHandler());
        netServer.listen(port, res -> {
            if (res.succeeded()) {
                log.info("trible tcp server 启动成功,监听端口：{}", port);
            } else {
                log.error("trible tcp server 启动失败,错误信息：{}", res.cause().getMessage());
            }
        });

    }

}
