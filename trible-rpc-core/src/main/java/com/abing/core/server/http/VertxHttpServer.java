package com.abing.core.server.http;

import com.abing.core.server.NetWork;
import io.vertx.core.Vertx;

/**
 * @Author CaptainBing
 * @Date 2024/9/26 17:22
 * @Description
 */
public class VertxHttpServer implements NetWork {

    @Override
    public void doStart(int port) {

        Vertx vertx = Vertx.vertx();
        vertx.createHttpServer()
             .requestHandler(new HttpServerHandler())
             .listen(port,result -> {
                if (result.succeeded()) {
                    System.out.println("Server started on port " + port);
                } else {
                    System.out.println("Failed to start server: " + result.cause().getMessage());
                }
             });
    }
}
