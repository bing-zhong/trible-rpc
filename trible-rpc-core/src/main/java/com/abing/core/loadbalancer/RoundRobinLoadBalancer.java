package com.abing.core.loadbalancer;

import com.abing.core.model.registry.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author CaptainBing
 * @Date 2024/10/17 18:19
 * @Description 轮询负载均衡
 */
public class RoundRobinLoadBalancer implements LoadBalancer{

    private AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {

        if (Objects.isNull(serviceMetaInfoList) || serviceMetaInfoList.isEmpty()) {
            return null;
        }
        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        int index = currentIndex.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
