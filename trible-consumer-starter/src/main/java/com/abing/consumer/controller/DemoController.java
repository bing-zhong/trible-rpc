package com.abing.consumer.controller;

import com.abing.common.service.DemoService;
import com.abing.trible.annotation.TribleReference;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author CaptainBing
 * @Date 2024/10/18 18:23
 * @Description
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @TribleReference
    private DemoService demoService;

    @GetMapping("/greeting/{name}")
    public String greeting(@PathVariable String name) {
        return demoService.greeting(name);
    }


}
