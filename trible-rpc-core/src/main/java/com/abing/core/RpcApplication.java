package com.abing.core;

import com.abing.core.config.RpcConfig;
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
        log.info("trible rpc init success,config:{}",rpcConfig);
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
