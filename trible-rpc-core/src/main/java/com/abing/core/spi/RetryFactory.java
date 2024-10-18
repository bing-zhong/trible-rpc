package com.abing.core.spi;

import com.abing.core.fault.retry.RetryStrategy;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 10:11
 * @Description
 */
public class RetryFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    public static RetryStrategy getInstance(String key){
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }


}
