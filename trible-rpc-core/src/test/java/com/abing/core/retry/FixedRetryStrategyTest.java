package com.abing.core.retry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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