package com.abing.core.bootstrap;

import com.abing.core.model.ServiceRegisterInfo;

import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 17:00
 * @Description
 */
public interface Bootstrap {

    /**
     * 初始化服务提供者
     * @param serviceRegisterInfoList
     */
    void initProvider(List<ServiceRegisterInfo<?>> serviceRegisterInfoList);

    /**
     * 初始化服务消费者
     */
    void initConsumer();

}
