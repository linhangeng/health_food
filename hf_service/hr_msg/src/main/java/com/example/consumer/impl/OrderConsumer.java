package com.example.consumer.impl;


import com.example.consumer.QueueConsumer;
import com.example.model.dto.Order;
import org.springframework.stereotype.Component;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/21 22:35
 */
@Component
public class OrderConsumer implements QueueConsumer<Order> {


    @Override
    public String getQueueName() {
        return "order.queue";
    }

    @Override
    public Class<Order> getMessageType() {
        return Order.class;
    }

    @Override
    public void handleMessage(Order message) {
        // 处理订单消息
        System.out.println("Received order: " + message);
    }
}

