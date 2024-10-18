package com.abing.core.spi;

import com.abing.core.fault.tolerant.TolerantStrategy;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 16:22
 * @Description
 */
public class TolerantFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }
    public static TolerantStrategy getInstance(String key){
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }

}
