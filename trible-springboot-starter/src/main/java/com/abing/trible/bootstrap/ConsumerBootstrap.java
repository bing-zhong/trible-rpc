package com.abing.trible.bootstrap;

import com.abing.trible.annotation.TribleReference;
import com.abing.trible.proxy.ServiceProxy;
import com.abing.trible.proxy.ServiceProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 18:08
 * @Description
 */
@Component
public class ConsumerBootstrap implements BeanPostProcessor {

    private final ServiceProxy serviceProxy;
    @Lazy
    public ConsumerBootstrap(ServiceProxy serviceProxy) {
        this.serviceProxy = serviceProxy;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            TribleReference tribleReference = field.getAnnotation(TribleReference.class);
            if (Objects.nonNull(tribleReference)) {
                Class<?> interfaceClass = tribleReference.interfaceClass();
                if (interfaceClass == void.class) {
                    interfaceClass = field.getType();
                }
                field.setAccessible(true);
                Object proxy = ServiceProxyFactory.getProxy(interfaceClass, serviceProxy);
                try {
                    field.set(bean, proxy);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("注入对象失败",e);
                }
                field.setAccessible(false);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
