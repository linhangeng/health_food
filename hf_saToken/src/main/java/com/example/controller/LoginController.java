package com.example.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName LoginController
 * @Author sheng.lin
 * @Date 2025/7/25
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@RequestMapping("/user")
@RestController()
@Tag(name = "sa-token", description = "sa-token")
public class LoginController {


    // 会话登录接口
    @GetMapping("/doLogin")
    public SaResult doLogin(@RequestParam("name") String name, @RequestParam("pwd") String pwd) {
        // 第一步：比对前端提交的账号名称、密码
        if ("zhang".equals(name) && "123456".equals(pwd)) {
            // 第二步：根据账号id，进行登录
            StpUtil.login(10001);
//            StpUtil.
            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }

    // 查询登录状态  ---- http://localhost:8081/acc/isLogin
    @GetMapping("/isLogin")
    public SaResult isLogin() {
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    // 查询 Token 信息  ---- http://localhost:8081/acc/tokenInfo
    @GetMapping("/tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    // 测试注销  ---- http://localhost:8081/acc/logout
    @GetMapping("/logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

}
