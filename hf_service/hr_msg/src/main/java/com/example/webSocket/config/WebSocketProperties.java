package com.example.webSocket.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName WebSocketProperties
 * @Author sheng.lin
 * @Date 2025/7/14
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Data
@ConfigurationProperties(prefix = "msg.ws")
public class WebSocketProperties {

    /**
     * 发送时间的限制，默认3秒，单位：毫秒
     */
    @Value("${sendTimeLimit:3000}")
    private Integer sendTimeLimit;

    /**
     * 发送消息缓冲上线，5MB 1024*1024*5
     */
    @Value("${bufferSizeLimit:5242880}")
    private Integer bufferSizeLimit;

    /**
     * 核心线程池数量，默认10个
     */
    @Value("${coreThreadSize:10}")
    private Integer coreThreadSize;

    /**
     * 最大线程池数量，默认50个
     */
    @Value("${maxThreadSize:50}")
    private Integer maxThreadSize;

    /**
     * 消息队列容量，默认100
     */
    @Value("${queueCapacity:100}")
    private Integer queueCapacity;

}
