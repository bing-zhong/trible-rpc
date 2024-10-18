package com.abing.core.retry;

import com.abing.core.fault.retry.FixedRetryStrategy;
import org.junit.jupiter.api.Test;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 15:07
 * @Description
 */
class FixedRetryStrategyTest {

    @Test
    void doRetry() throws Exception {

        FixedRetryStrategy fixedRetryStrategy = new FixedRetryStrategy();
        fixedRetryStrategy.doRetry(()->{
            System.out.println("test retry");
            throw new RuntimeException("test exception");
        });

    }
}