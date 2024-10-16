package com.abing.core.server.http;

import com.abing.core.model.api.RpcRequest;
import com.abing.core.model.api.RpcResponse;
import com.abing.core.register.LocalRegistry;
import com.abing.core.serialize.HessianSerializer;
import com.abing.core.serialize.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @Author CaptainBing
 * @Date 2024/9/30 9:58
 * @Description http请求消息处理器
 */
@Slf4j
public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {

        Serializer serializer = new HessianSerializer();
        log.info("request receive method: {}, uri: {}", request.method(), request.uri());
        request.bodyHandler(body -> {
            byte[] bodyBytes = body.getBytes();
            try {
                RpcRequest rpcRequest = serializer.deserialize(bodyBytes, RpcRequest.class);

                String serviceName = rpcRequest.getServiceName();
                String methodName = rpcRequest.getMethodName();

                Class<?> serviceClass = LocalRegistry.get(serviceName);
                Method method = serviceClass.getMethod(methodName, rpcRequest.getParameterTypes());
                Object result = method.invoke(serviceClass.newInstance(), rpcRequest.getArgs());
                log.info("request receive body: {}", rpcRequest);

                RpcResponse rpcResponse = RpcResponse
                        .builder()
                        .data(result)
                        .dataType(method.getReturnType())
                        .message("ok")
                        .build();
                byte[] responseBytes = serializer.serialize(rpcResponse);
                request.response().end(Buffer.buffer(responseBytes));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
