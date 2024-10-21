package com.abing.trible.annotation;

import com.abing.trible.bootstrap.RpcInitBootstrap;
import com.abing.trible.bootstrap.TribleConsumerBootstrap;
import com.abing.trible.bootstrap.TribleProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 17:45
 * @Description
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, TribleProviderBootstrap.class, TribleConsumerBootstrap.class})
public @interface EnableTrible {

    boolean needServer() default true;

}
