package com.abing.core.loadbalancer;

import com.abing.core.model.registry.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 18:15
 * @Description
 */
public interface LoadBalancer {

    /**
     * 服务选择调用
     * @param serviceMetaInfoList
     * @param requestParams
     * @return
     */
    ServiceMetaInfo select(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);

}
