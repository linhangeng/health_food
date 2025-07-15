package com.example.webSocket.ws;

/**
 * @ClassName MyWebSocketConfig
 * @Author sheng.lin
 * @Date 2025/7/14
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */

import com.example.webSocket.config.WebSocketProperties;
import com.example.webSocket.session.WebSocketSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 复用示例
 */

@Configuration
public class MyWebSocketConfig {


    @Bean
    public CustomParamWebSocketHandler userWebSocketHandler(WebSocketProperties properties) {
        // 自定义URL路径、URI模板和参数名
        return new CustomParamWebSocketHandler(
                properties,
                "/user/*",           // URL路径模式
                "/user/{userId}",    // URI模板
                "userId"             // 参数名
        );
    }

    @Bean
    public CustomParamWebSocketHandler roomWebSocketHandler(WebSocketProperties properties) {
        return new CustomParamWebSocketHandler(
                properties,
                "/room/*",
                "/room/{roomId}",
                "roomId"
        );
    }
}


