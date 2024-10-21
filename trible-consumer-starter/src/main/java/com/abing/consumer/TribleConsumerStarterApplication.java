package com.abing.consumer;

import com.abing.trible.annotation.EnableTrible;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableTrible
@SpringBootApplication
public class TribleConsumerStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TribleConsumerStarterApplication.class, args);
    }

}
