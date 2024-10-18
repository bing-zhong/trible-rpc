package com.abing.core.retry;

import com.abing.core.RpcApplication;
import com.abing.core.config.RpcConfig;
import com.abing.core.model.api.RpcResponse;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 10:25
 * @Description 固定间隔重试策略
 */
@Slf4j
public class FixedRetryStrategy implements RetryStrategy {

    private RpcConfig rpcConfig = RpcApplication.getRpcConfig();

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        RetryConfig retryConfig = rpcConfig.getRetry();
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                                                   .retryIfExceptionOfType(Exception.class)
                                                   .withWaitStrategy(WaitStrategies.fixedWait(retryConfig.getInterval(), TimeUnit.SECONDS))
                                                   .withStopStrategy(StopStrategies.stopAfterAttempt(retryConfig.getInterval()))
                                                   .withRetryListener(new RetryListener() {
                                                       @Override
                                                       public <V> void onRetry(Attempt<V> attempt) {
                                                           if (attempt.hasException()) {
                                                               log.info("重试次数：{},异常信息：{}", attempt.getAttemptNumber(), attempt
                                                                       .getExceptionCause()
                                                                       .getMessage());
                                                           }
                                                       }
                                                   })
                                                   .build();
        return retryer.call(callable);
    }
}
