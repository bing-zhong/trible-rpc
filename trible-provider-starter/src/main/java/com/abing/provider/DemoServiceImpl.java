package com.abing.provider;

import com.abing.common.service.DemoService;
import com.abing.trible.annotation.TribleService;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 18:24
 * @Description
 */
@TribleService
public class DemoServiceImpl implements DemoService {

    @Override
    public String greeting(String name) {
        return "Hello " + name;
    }
}
