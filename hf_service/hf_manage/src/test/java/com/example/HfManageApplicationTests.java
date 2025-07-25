package com.example;

import com.example.service.RedisService;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest(classes = HfManageApplication.class)
public class HfManageApplicationTests {

    @Autowired
    RedisService redisService;


    @Test
    public void redisTest() {
        redisService.set("111","ccc");

    }

}
