package com.abing.core.registry;

import cn.hutool.json.JSONUtil;
import com.abing.core.constant.RpcConstant;
import com.abing.core.model.registry.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author CaptainBing
 * @Date 2024/10/10 16:29
 * @Description
 */
public class EtcdRegistry implements Registry {

    private Client client;

    private KV kvClient;

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                       .endpoints(registryConfig.getAddress())
                       .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                       .build();
        kvClient = client.getKVClient();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) {

        Lease leaseClient = client.getLeaseClient();
        long leaseId = leaseClient.grant(30).join().getID();
        String registryKey = RpcConstant.REGISTRY_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key,value,putOption).join();

    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String deleteKey = RpcConstant.REGISTRY_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(deleteKey, StandardCharsets.UTF_8)).join();
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscover(String serviceName) {
        // 开启前缀搜索
        GetOption getOption = GetOption
                .builder()
                .isPrefix(true)
                .build();
        String key = RpcConstant.REGISTRY_ROOT_PATH + serviceName;
        List<KeyValue> keyValues = kvClient
                .get(ByteSequence.from(key, StandardCharsets.UTF_8), getOption)
                .join()
                .getKvs();
        return keyValues.stream()
                        .map(keyValue -> JSONUtil.toBean(keyValue.getValue().toString(StandardCharsets.UTF_8),ServiceMetaInfo.class))
                        .collect(Collectors.toList());
    }

    @Override
    public void destroy() {
        if (Objects.nonNull(client)) {
            client.close();
        }
        if (Objects.nonNull(kvClient)){
            kvClient.close();
        }
    }
}
