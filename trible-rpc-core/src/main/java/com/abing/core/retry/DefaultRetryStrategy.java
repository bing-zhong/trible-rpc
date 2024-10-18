package com.abing.core.retry;

import com.abing.core.model.api.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 10:08
 * @Description 默认重试策略 不重试
 */
public class DefaultRetryStrategy implements RetryStrategy {
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
