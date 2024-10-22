package com.abing.trible.annotation;

import com.abing.trible.bootstrap.ServerBootstrap;
import com.abing.trible.bootstrap.ConsumerBootstrap;
import com.abing.trible.bootstrap.ProviderBootstrap;
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
@Import({ServerBootstrap.class, ProviderBootstrap.class, ConsumerBootstrap.class})
public @interface EnableTrible {

    boolean needServer() default true;

}
