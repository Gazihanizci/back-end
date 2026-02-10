package com.example.finansapii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FinansApiiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinansApiiApplication.class, args);
    }

}
