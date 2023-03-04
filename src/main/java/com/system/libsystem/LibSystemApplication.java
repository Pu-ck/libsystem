package com.system.libsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LibSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibSystemApplication.class, args);
    }

}
