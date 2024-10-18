package com.abing.core.fault.retry;

import lombok.Data;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 15:58
 * @Description
 */
@Data
public class RetryConfig {


    /**
     * 重试策略类型
     */
    private RetryKeys type = RetryKeys.DEFAULT;

    /**
     * 固定重试频率
     */
    private int interval = 3;

}
