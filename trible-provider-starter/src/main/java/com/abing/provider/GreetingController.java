package com.abing.provider;

import com.abing.common.service.GreetingService;
import com.abing.trible.annotation.TribleReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author CaptainBing
 * @Date 2024/10/21 10:19
 * @Description
 */
@RestController
@RequestMapping
public class GreetingController {

    @TribleReference
    private GreetingService greetingService;

    @GetMapping("/greeting")
    public String greeting(String name){
        return greetingService.greeting(name);
    }


}
