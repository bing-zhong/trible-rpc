package com.abing.core;

import com.abing.core.config.RpcConfig;
import com.abing.core.registry.Registry;
import com.abing.core.registry.RegistryConfig;
import com.abing.core.spi.RegistryFactory;
import com.abing.core.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @Author CaptainBing
 * @Date 2024/10/9 15:55
 * @Description
 */
@Slf4j
public class RpcApplication {

    public static volatile RpcConfig rpcConfig;

    /**
     * 初始化配置
     */
    public static void init(){
        RpcConfig loadConfig = null;
        try {
            loadConfig = ConfigUtils.loadConfig(RpcConfig.class);
        }catch (Exception e){
            loadConfig = new RpcConfig();
        }
        rpcConfig = loadConfig;
        log.info("trible rpc config init success:{}",rpcConfig);
        RegistryConfig registryConfig = rpcConfig.getRegistry();
        Registry registry = RegistryFactory.getInstance(registryConfig.getType().name());
        registry.init(registryConfig);
        // 服务提供者正常下线，清理注册中心信息
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    /**
     * 获取rpc配置
     * @return
     */
    public static RpcConfig getRpcConfig(){
        if(Objects.isNull(rpcConfig)){
            synchronized (RpcApplication.class){
                if(Objects.isNull(rpcConfig)){
                    init();
                }
            }
        }
        return rpcConfig;
    }

}
