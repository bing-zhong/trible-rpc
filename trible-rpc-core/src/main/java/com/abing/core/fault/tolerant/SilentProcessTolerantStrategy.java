package com.abing.core.fault.tolerant;

import com.abing.core.model.api.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 16:18
 * @Description 静默处理 返回空数据
 */
@Slf4j
public class SilentProcessTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("SilentProcessTolerantStrategy doTolerant: {}", e.getMessage());
        return new RpcResponse();
    }
}
