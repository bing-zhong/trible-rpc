package com.abing.core.registry;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.abing.core.constant.RpcConstant;
import com.abing.core.model.registry.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
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

    private final Set<String> watchingKeySet = new HashSet<>();

    @Override
    public void init(RegistryConfig registryConfig) {

        client = Client.builder()
                       .endpoints(registryConfig.getAddress())
                       .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                       .build();
        kvClient = client.getKVClient();
        heartbeat();
        log.info("etcd registry init success: {}",registryConfig);

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
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(key + ":续签失败", e);
                }
            }
        });
        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        if (!CronUtil.getScheduler().isStarted()) {
            CronUtil.start();
        }
    }

    @Override
    public void watch(String serviceNodeKey) {
        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        if (newWatch) {
            Watch watchClient = client.getWatchClient();
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8),watchResponse -> {
                for (WatchEvent event : watchResponse.getEvents()) {
                    switch (event.getEventType()) {
                        case PUT:
                            LOCAL_REGISTER_NODE_KEY_SET.add(serviceNodeKey);
                            break;
                        case DELETE:
                            registryServiceCache.clearCache();
                            log.info("服务节点:{} 下线", serviceNodeKey);
                            break;
                        default:break;
                    }
                }
            });

        }
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) {

        Lease leaseClient = client.getLeaseClient();
        int expireTime = 30;
        try {
            long leaseId = leaseClient.grant(expireTime).get(expireTime, TimeUnit.SECONDS).getID();
            String registryKey = RpcConstant.REGISTRY_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
            ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
            ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);
            PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
            kvClient.put(key,value,putOption).get();
            LOCAL_REGISTER_NODE_KEY_SET.add(registryKey);
        } catch (Exception e) {
            throw new RuntimeException("服务节点 注册失败:" + serviceMetaInfo.getServiceNodeKey(), e);
        }


    }

    @Override
    public void unRegister(String serviceNodeKey) {

        String deleteKey = RpcConstant.REGISTRY_ROOT_PATH + serviceNodeKey;
        try {
            kvClient.delete(ByteSequence.from(deleteKey, StandardCharsets.UTF_8)).get();
            log.info("服务节点:{} 正常下线",serviceNodeKey);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(serviceNodeKey + "服务下线异常", e);
        }
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
        List<KeyValue> keyValues = null;
        try {
            keyValues = kvClient
                    .get(ByteSequence.from(RpcConstant.REGISTRY_ROOT_PATH + serviceName, StandardCharsets.UTF_8), getOption)
                    .get()
                    .getKvs();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("获取服务异常",e);
        }
        List<ServiceMetaInfo> serviceMetaInfoList =
                keyValues.stream()
                         .map(keyValue -> {
                             String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                             watch(key);
                             String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                             return JSONUtil.toBean(value, ServiceMetaInfo.class);
                         })
                         .collect(Collectors.toList());
        registryServiceCache.writeCache(serviceMetaInfoList);
        return serviceMetaInfoList;

    }

    @Override
    public void destroy() {
        CronUtil.stop();
        log.info("rpc heartbeat stop success");
        // 服务节点下线
        LOCAL_REGISTER_NODE_KEY_SET.forEach(this::unRegister);
        // 关闭资源
        if (Objects.nonNull(client)) {
            client.close();
        }
        if (Objects.nonNull(kvClient)){
            kvClient.close();
        }

    }
}
