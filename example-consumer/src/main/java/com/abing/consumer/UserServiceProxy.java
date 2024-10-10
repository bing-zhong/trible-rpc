package com.abing.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.abing.common.model.User;
import com.abing.common.service.UserService;
import com.abing.core.model.api.RpcRequest;
import com.abing.core.model.api.RpcResponse;
import com.abing.core.serialize.JdkSerializer;
import com.abing.core.serialize.Serializer;

import java.io.IOException;

/**
 * @Author CaptainBing
 * @Date 2024/9/30 11:37
 * @Description
 */
public class UserServiceProxy implements UserService {


    @Override
    public User getUser(User user) {

        Serializer serializer = new JdkSerializer();

        RpcRequest rpcRequest = RpcRequest.builder()
                                          .serviceName(UserService.class.getName())
                                          .methodName("getUser")
                                          .parameterTypes(new Class[]{User.class})
                                          .args(new Object[]{user})
                                          .build();

        try {
            byte[] requestBytes = serializer.serialize(rpcRequest);
            HttpResponse httpResponse = HttpRequest.post("http://localhost:8888")
                                                   .body(requestBytes)
                                                   .execute();
            byte[] bodyBytes = httpResponse.bodyBytes();
            RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
