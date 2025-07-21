package com.example.config;

import com.example.Properties.QueueConfig;
import com.example.Properties.RabbitQueuesProperties;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

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
@EnableConfigurationProperties(RabbitQueuesProperties.class)
public class rabbitmqConfig {


    @Resource
    RabbitQueuesProperties queuesProperties;

    /**
     * 声明多个队列为 Spring Bean（自动通过 RabbitAdmin 发布）
     * @param
     * @Author sheng.lin
     * @Date 2025/7/21
     * @return: org.springframework.amqp.core.Declarables
     * @Version  1.0
     * @修改记录
     */
    @Bean
    public Declarables queueDeclarables() {
        List<Declarable> declarables = new ArrayList<>();
        for (QueueConfig cfg : queuesProperties.getQueues()) {
            Queue queue = QueueBuilder.durable(cfg.getName())
                    // 可选：设置死信队列属性
                    .withArgument("x-dead-letter-exchange", "")
                    .withArgument("x-dead-letter-routing-key", cfg.getName() + ".dlq")
                    .build();
            declarables.add(queue);
        }
        return new Declarables(declarables);
    }



    /**
     * JSON 转换器，用于自动将消息与 Java 对象互转
     * @param
     * @Author sheng.lin
     * @Date 2025/7/21
     * @return: org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
     * @Version  1.0
     * @修改记录
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    /**
     *  RabbitTemplate 封装，注入 JSON 转换器
     * @param connectionFactory
     * @Author sheng.lin
     * @Date 2025/7/21
     * @return: org.springframework.amqp.rabbit.core.RabbitTemplate
     * @Version  1.0
     * @修改记录
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        return template;
    }
}
