package com.makarenko.rollbackservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.jms.annotation.EnableJms;

@EnableEurekaClient
@SpringBootApplication
@EnableJms
public class RollBackServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RollBackServiceApplication.class, args);
    }
}
