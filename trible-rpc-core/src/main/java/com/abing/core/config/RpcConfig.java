package com.abing.core.config;

import com.abing.core.registry.RegistryConfig;
import com.abing.core.serialize.key.SerializerKeys;
import lombok.Data;

/**
 * @Author CaptainBing
 * @Date 2024/10/9 15:33
 * @Description
 */
@Data
public class RpcConfig {

    private String name = "trible-rpc";

    private String version = "1.0.0";

    private String host = "localhost";

    private Integer port = 7788;

    private SerializerKeys serialization = SerializerKeys.JDK;

    private RegistryConfig registryConfig = new RegistryConfig();

}
