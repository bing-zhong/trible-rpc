package com.abing.core.utils;

import cn.hutool.setting.dialect.Props;
import com.abing.core.constant.RpcConstant;

/**
 * @Author CaptainBing
 * @Date 2024/10/9 15:44
 * @Description 配置加载类
 */
public class ConfigUtils {

    private static final String CONFIG_FILE_NAME = "application.properties";

    public static <T> T loadConfig(Class<T> configClass, String prefix) {
        Props props = new Props(CONFIG_FILE_NAME);
        return props.toBean(configClass, prefix);
    }

    public static <T> T loadConfig(Class<T> configClass) {
        Props props = new Props(CONFIG_FILE_NAME);
        return props.toBean(configClass, RpcConstant.CONFIG_PREFIX);
    }

}
