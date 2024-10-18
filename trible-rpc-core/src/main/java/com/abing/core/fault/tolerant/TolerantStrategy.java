package com.abing.core.fault.tolerant;

import com.abing.core.model.api.RpcResponse;

import java.util.Map;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 16:13
 * @Description
 */
public interface TolerantStrategy {

    /**
     * 容错机制
     * @param context
     * @param e
     * @return
     */
    RpcResponse doTolerant(Map<String,Object> context, Exception e);

}
