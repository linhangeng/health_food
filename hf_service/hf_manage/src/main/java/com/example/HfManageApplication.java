package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.example",exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = {"com.example"})
@EnableDiscoveryClient
public class HfManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(HfManageApplication.class, args);
    }

}
