package com.abing.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 16:56
 * @Description 服务注册信息类
 */
@Data
@AllArgsConstructor
public class ServiceRegisterInfo<T> {
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务实现类
     */
    private Class<? extends T> serviceClass;

}
