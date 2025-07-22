package com.example.producer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpIOException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/21 22:21
 */
@Service
@Slf4j
public class RabbitProducer {

    @Resource
    RabbitTemplate rabbitTemplate;


    @PostConstruct
    public void init() {
        // 消息确认回调（消息是否成功到达交换机）
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息成功发送到交换机，correlationId: {}", correlationData.getId());
            } else {
                log.error("消息发送到交换机失败，原因: {}", cause);
                // 可实现重试逻辑
            }
        });
        // 消息返回回调（消息无法路由到队列时触发）
        rabbitTemplate.setReturnsCallback(returned -> {
            log.error("消息路由失败，交换机: {}, 路由键: {}, 回复码: {}, 回复文本: {}",
                    returned.getExchange(),
                    returned.getRoutingKey(),
                    returned.getReplyCode(),
                    returned.getReplyText());
        });
    }


    /**
     * 消息发送
     * @param exchange
     * @param routingKey
     * @param message
     * @Author sheng.lin
     * @Date 2025/7/21
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    public void send(String exchange, String routingKey, Object message) {
        // 若 空字符串的exchange 代表rabbitmq的默认交换机
        // 交换机：在 RabbitMQ 中，exchange（交换机）是消息的 “中转站”，
        // 它的核心作用是接收生产者发送的消息，并根据绑定规则（绑定键与路由键的匹配关系）将消息路由到对应的队列。
        // 交换机是 RabbitMQ 消息模型的核心组件，所有生产者发送的消息必须经过交换机才能到达队列
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }


    /**
     * 发送带TTL的消息
     * @param routingKey
     * @param message
     * @param ttlMillis
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    public void sendWithTTL(String routingKey, Object message, int ttlMillis) {
        rabbitTemplate.convertAndSend("", routingKey, message, msg -> {
            msg.getMessageProperties().setExpiration(String.valueOf(ttlMillis));
            return msg;
        });
    }


    /**
     * 发送延迟消息（需配合RabbitMQ延迟插件）
     * @param routingKey
     * @param message
     * @param delayMillis
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    public void sendDelayed(String routingKey, Object message, int delayMillis) {
        rabbitTemplate.convertAndSend("x-delayed-exchange", routingKey, message, msg -> {
            msg.getMessageProperties().setHeader("x-delay", delayMillis);
            return msg;
        });
    }

    /**
     * 发送带确认机制的消息
     * @param exchange
     * @param routingKey
     * @param message
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    public void sendWithConfirm(String exchange, String routingKey, Object message) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
    }


    /**
     * 发送消息并支持重试
     * @param exchange
     * @param routingKey
     * @param message
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    public void sendWithRetry(String exchange, String routingKey, Object message) {
        RetryTemplate retryTemplate = RetryTemplate.builder()
                .maxAttempts(3)
                .fixedBackoff(1000) // 重试间隔1秒
                .retryOn(AmqpIOException.class) // 只对网络异常重试
                .build();
        retryTemplate.execute(context -> {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            return null;
        });
    }
}

