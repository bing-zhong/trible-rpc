package com.abing.core.spi;

import com.abing.core.registry.Registry;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 17:07
 * @Description
 */
public class RegistryFactory {

    static {
        SpiLoader.load(Registry.class);
    }

    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class, key);
    }

}
