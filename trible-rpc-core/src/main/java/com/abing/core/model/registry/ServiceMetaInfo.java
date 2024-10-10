package com.abing.core.model.registry;

import com.abing.core.constant.RpcConstant;
import lombok.Data;


/**
 * @Author CaptainBing
 * @Date 2024/10/10 15:28
 * @Description
 */
@Data
public class ServiceMetaInfo {

    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务版本
     */
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;
    /**
     * 服务地址
     */
    private String serviceHost;
    /**
     * 服务端口
     */
    private Integer servicePort;
    /**
     * 服务分组
     */
    private String group = RpcConstant.DEFAULT_SERVICE_GROUP;

    /**
     * 获取服务key
     * @return
     */
    public String getServiceKey() {
        return String.format("%s:%s:%s", serviceName, serviceVersion, group);
    }

    /**
     * 获取服务节点key
     * @return
     */
    public String getServiceNodeKey() {
        return String.format("%s:%s:%s:%s:%s", serviceName, serviceVersion, group, serviceHost,servicePort);
    }

}
