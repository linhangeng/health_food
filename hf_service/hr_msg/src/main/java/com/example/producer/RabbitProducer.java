package com.example.producer;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/21 22:21
 */
@Service
public class RabbitProducer {

    @Resource
    RabbitTemplate rabbitTemplate;

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
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}

