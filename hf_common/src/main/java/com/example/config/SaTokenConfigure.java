package com.example.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName SaTokenConfigure
 * @Author sheng.lin
 * @Date 2025/7/25
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {

            // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
            SaRouter.match("/**", "/user/doLogin", r -> StpUtil.checkLogin());

            // 角色校验 -- 拦截以 admin 开头的路由，必须具备 admin 角色或者 super-admin 角色才可以通过认证
            SaRouter.match("/admin/**", r -> StpUtil.checkRoleOr("admin", "super-admin"));

            // 权限校验 -- 不同模块校验不同权限
            SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
            SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
            SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
            SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
            SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
            SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));

            // 甚至你可以随意的写一个打印语句
            SaRouter.match("/**", r -> System.out.println("----啦啦啦----"));

            // 连缀写法
            SaRouter.match("/**").check(r -> System.out.println("----啦啦啦----"));

        })).addPathPatterns("/**");
    }
}

