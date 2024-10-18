package com.abing.core.fault.retry;

import com.abing.core.model.api.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 9:45
 * @Description 重试策略
 */
public interface RetryStrategy {

    /**
     * 错误重试
     * @param callable
     * @return
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;


}
