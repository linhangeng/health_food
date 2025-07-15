package com.example.webSocket.config;

import com.example.webSocket.send.WebSocketMessageSender;
import com.example.webSocket.ws.CustomParamWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

import java.util.List;

/**
 * @ClassName WebSocketAutoConfiguration
 * @Author sheng.lin
 * @Date 2025/7/14
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
/**
 * 开启 websocket
 */
@Slf4j
@EnableConfigurationProperties(WebSocketProperties.class)
@EnableWebSocket
@Configuration
public class WebSocketAutoConfiguration implements InitializingBean {

    private final WebSocketProperties webSocketProperties;

//    private final TokenApi tokenApi;

    public WebSocketAutoConfiguration(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }

    @Bean
    public WebSocketConfigurer defaultWebSocketConfigurer(List<CustomParamWebSocketHandler> customParamWebSocketHandlers) {
        return registry -> {
            for (CustomParamWebSocketHandler customParamWebSocketHandler : customParamWebSocketHandlers) {
                registry.addHandler(customParamWebSocketHandler, customParamWebSocketHandler.getUrlPath())
                        .setAllowedOrigins("*");
                log.info("注册 WebSocketHandler, 连接路径:{}, 路径模板:{}, 连接参数:{}",
                        customParamWebSocketHandler.getUrlPath(),
                        customParamWebSocketHandler.getUriTemplate(),
                        customParamWebSocketHandler.getParamKey());
            }
        };
    }


    /**
     * 设置默认websocket连接参数
     * @param
     * @Author sheng.lin
     * @Date 2025/7/15
     * @return: com.example.webSocket.ws.CustomParamWebSocketHandler
     * @Version  1.0
     * @修改记录
     */
    @Primary
    @Bean("defaultWebSocketHandler")
    public CustomParamWebSocketHandler customParamWebSocketHandler() {
        return new CustomParamWebSocketHandler(webSocketProperties);
    }

    /**
     * 初始化连接池
     * @param
     * @Author sheng.lin
     * @Date 2025/7/15
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    @Override
    public void afterPropertiesSet() {
        // 初始化ws消息发送的线程池
        WebSocketMessageSender.initializeExecutor(webSocketProperties);
    }
}

