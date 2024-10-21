package com.abing.trible.annotation;

import com.abing.core.constant.RpcConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 17:46
 * @Description
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface TribleService {

    Class<?> interfaceClass() default void.class;

    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

}
