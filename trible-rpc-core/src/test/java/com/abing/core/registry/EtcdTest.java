package com.abing.core.registry;

import cn.hutool.json.JSONUtil;
import com.abing.core.constant.RpcConstant;
import com.abing.core.model.registry.ServiceMetaInfo;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.Lease;
import io.etcd.jetcd.options.PutOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @Author CaptainBing
 * @Date 2024/10/10 16:08
 * @Description
 */
class EtcdTest {

    private static Client client;

    private static KV kvClient;

    @BeforeAll
    static void init() {
        client = Client.builder()
              .endpoints("http://localhost:2379")
              .build();
        kvClient = client.getKVClient();
    }

    @Test
    void testPut(){
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(8080);
        serviceMetaInfo.setServiceName("com.abing.user");
        Lease leaseClient = client.getLeaseClient();
        long leaseId = leaseClient.grant(30).join().getID();
        String registryKey = RpcConstant.REGISTRY_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key,value,putOption).join();
    }

    @Test
    void testDelete(){
        kvClient.delete(bytesOf("/abc/test/test1")).join();
    }

    /**
     * 将字符串转为客户端所需的ByteSequence实例
     * @param val
     * @return
     */
    private static ByteSequence bytesOf(String val) {
        return ByteSequence.from(val, UTF_8);
    }


}