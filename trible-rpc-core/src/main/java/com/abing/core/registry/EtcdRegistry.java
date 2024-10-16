package com.abing.core.registry;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.abing.core.constant.RpcConstant;
import com.abing.core.model.registry.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @Author CaptainBing
 * @Date 2024/10/10 16:29
 * @Description
 */
@Slf4j
public class EtcdRegistry implements Registry {

    private Client client;

    private KV kvClient;

    private final static Set<String> LOCAL_REGISTER_NODE_KEY_SET = new HashSet<>();

    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                       .endpoints(registryConfig.getAddress())
                       .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                       .build();
        kvClient = client.getKVClient();
        heartbeat();
    }

    @Override
    public void heartbeat() {
        String execByPer10Second = "*/10 * * * * *";
        CronUtil.schedule(execByPer10Second, (Task) () -> {
            for (String key : LOCAL_REGISTER_NODE_KEY_SET) {

                try {
                    List<KeyValue> keyValueList = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                                          .get()
                                                          .getKvs();
                    if (keyValueList.isEmpty()) {
                        continue;
                    }
                    KeyValue keyValue = keyValueList.get(0);
                    String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                    ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                    register(serviceMetaInfo);
                    log.info("续签成功:{}", serviceMetaInfo);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(key + ":续签失败", e);
                }
            }
        });
        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
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
        LOCAL_REGISTER_NODE_KEY_SET.add(registryKey);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String deleteKey = RpcConstant.REGISTRY_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(deleteKey, StandardCharsets.UTF_8)).join();
        LOCAL_REGISTER_NODE_KEY_SET.remove(deleteKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscover(String serviceName) {
        List<ServiceMetaInfo> serviceMetaInfoCache = registryServiceCache.readCache();
        if (Objects.nonNull(serviceMetaInfoCache)) {
            return serviceMetaInfoCache;
        }
        // 开启前缀搜索
        GetOption getOption = GetOption
                .builder()
                .isPrefix(true)
                .build();
        String key = RpcConstant.REGISTRY_ROOT_PATH + serviceName;
        List<KeyValue> keyValues = null;
        try {
            keyValues = kvClient
                    .get(ByteSequence.from(key, StandardCharsets.UTF_8), getOption)
                    .get()
                    .getKvs();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("获取服务异常",e);
        }
        List<ServiceMetaInfo> serviceMetaInfoList =
                keyValues.stream()
                         .map(keyValue -> JSONUtil.toBean(keyValue.getValue().toString(StandardCharsets.UTF_8), ServiceMetaInfo.class))
                         .collect(Collectors.toList());
        registryServiceCache.writeCache(serviceMetaInfoList);
        return serviceMetaInfoList;
    }

    @Override
    public void destroy() {

        // 服务节点下线
        for (String key : LOCAL_REGISTER_NODE_KEY_SET) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
                log.info("服务节点:{} 正常下线",key);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(key + "服务下线异常", e);
            }
        }

        if (Objects.nonNull(client)) {
            client.close();
        }
        if (Objects.nonNull(kvClient)){
            kvClient.close();
        }
    }
}
