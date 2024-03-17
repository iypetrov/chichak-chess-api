package com.example.chichakchessapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableRetry
@EnableScheduling
public class ChichakChessApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChichakChessApiApplication.class, args);
    }
}