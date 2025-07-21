package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Queue;

/**
 * @ClassName rabbitmq
 * @Author sheng.lin
 * @Date 2025/7/21
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Configuration
public class rabbitmqConfig {


    /**
     * 工作队列模式：一对多的消息传递，一个生产者发送消息到一个队列，多个消费者竞争消费消息（负载均衡）
     * @param
     * @Author sheng.lin
     * @Date 2025/7/21
     * @return: java.util.Queue
     * @Version  1.0
     * @修改记录
     */
    @Bean
    public Queue workQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除
//        return new Queue();
        return null;
    }
}
