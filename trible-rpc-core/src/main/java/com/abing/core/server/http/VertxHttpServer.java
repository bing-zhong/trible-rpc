package com.abing.core.server.http;

import com.abing.core.server.Network;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author CaptainBing
 * @Date 2024/9/26 17:22
 * @Description
 */
@Slf4j
public class VertxHttpServer implements Network {

    @Override
    public void doStart(int port) {

        Vertx vertx = Vertx.vertx();
        vertx.createHttpServer()
             .requestHandler(new HttpServerHandler())
             .listen(port,result -> {
                if (result.succeeded()) {
                    log.info("Server started on port {}", port);
                } else {
                    log.info("Failed to start server: {}", result.cause().getMessage());
                }
             });
    }
}
