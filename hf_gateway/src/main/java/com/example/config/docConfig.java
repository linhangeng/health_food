//package com.example.config;
//
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @ClassName docConfig
// * @Author sheng.lin
// * @Date 2025/7/1
// * @Version 1.0
// * @修改记录
// **/
///*
// * projectName: healthyFood
// * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
// */
//@Configuration
//public class docConfig {
//
//
//    /**
//     * 核心服务
//     * @param
//     * @Author sheng.lin
//     * @Date 2025/7/1
//     * @return: org.springdoc.core.models.GroupedOpenApi
//     * @Version  1.0
//     * @修改记录
//     */
//    @Bean
//    public GroupedOpenApi userServiceApi() {
//        return GroupedOpenApi.builder()
//                .group("search-service")
//                .pathsToMatch("/search-service/**")
//                .build();
//    }
//
//
//
//    /**
//     * 边缘服务
//     * @param
//     * @Author sheng.lin
//     * @Date 2025/7/1
//     * @return: org.springdoc.core.models.GroupedOpenApi
//     * @Version  1.0
//     * @修改记录
//     */
////    @Bean
////    public GroupedOpenApi otherServicesApi() {
////        return GroupedOpenApi.builder()
////                .group("others")
////                .pathsToMatch("/**")
////                .packagesToExclude("com.example.user", "com.example.order")
////                .build();
////    }
//}
