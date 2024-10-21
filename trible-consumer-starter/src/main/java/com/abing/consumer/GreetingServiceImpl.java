package com.abing.consumer;

import com.abing.common.service.GreetingService;
import com.abing.trible.annotation.TribleService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author CaptainBing
 * @Date 2024/10/21 10:17
 * @Description
 */
@TribleService
@Slf4j
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greeting(String name) {
        log.info("greeting: {}", name);
        return "greeting " + name;
    }
}
