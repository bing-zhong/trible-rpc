package com.abing.trible.annotation;

import com.abing.core.constant.RpcConstant;
import com.abing.core.fault.retry.RetryKeys;
import com.abing.core.fault.tolerant.TolerantKeys;
import com.abing.core.loadbalancer.LoadBalancerKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 17:46
 * @Description
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TribleReference {

    Class<?> interfaceClass() default void.class;

    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    LoadBalancerKeys loadBalancer() default LoadBalancerKeys.RANDOM;

    RetryKeys retry() default RetryKeys.FIXED;

    TolerantKeys tolerant() default TolerantKeys.SILENTPROCESS;

}
