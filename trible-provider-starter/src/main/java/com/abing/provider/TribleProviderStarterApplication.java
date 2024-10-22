package com.abing.provider;

import com.abing.trible.annotation.EnableTrible;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableTrible
@SpringBootApplication
public class TribleProviderStarterApplication {

    public static void main(String[] args) {

        SpringApplication.run(TribleProviderStarterApplication.class, args);
    }
}
