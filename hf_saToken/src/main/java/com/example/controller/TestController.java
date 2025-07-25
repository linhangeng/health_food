package com.example.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Author sheng.lin
 * @Date 2025/7/25
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/hello")
    public String isLogin() {
        return "hello";
    }

}
