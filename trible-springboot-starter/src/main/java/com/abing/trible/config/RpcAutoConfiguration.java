package com.abing.trible.config;

import com.abing.trible.proxy.ServiceProxy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Author CaptainBing
 * @Date 2024/10/22 13:54
 * @Description
 */
@Data
@Slf4j
@EnableConfigurationProperties(RpcConfiguration.class)
public class RpcAutoConfiguration {

    @Bean
    public ServiceProxy serviceProxy(RpcConfiguration rpcConfiguration) {
        log.info("Initializing RpcConfiguration: {}",rpcConfiguration);
        return new ServiceProxy(rpcConfiguration);
    }


}
