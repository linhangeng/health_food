package com.example.consumer.impl;

import com.example.constants.RabbitMQConstants;
import com.example.consumer.DeadLetterQueueConsumer;
import com.example.model.dto.Order;
import org.springframework.stereotype.Component;

/**
 * @ClassName OrderDeadLetterConsumer
 * @Author sheng.lin
 * @Date 2025/7/22
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Component
public class OrderDeadLetterConsumer implements DeadLetterQueueConsumer<Order> {


    /**
     * 获取死信队列名称
     * @param
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: java.lang.String
     * @Version  1.0
     * @修改记录
     */
    @Override
    public String getDeadLetterQueueName() {
        return RabbitMQConstants.ORDER_DLQ;
    }


    /**
     * 获取消息类型
     * @param
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: java.lang.Class<com.example.model.dto.Order>
     * @Version  1.0
     * @修改记录
     */
    @Override
    public Class<Order> getDeadLetterMessageType() {
        return Order.class;
    }


    /**
     * 处理消息
     * @param message
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    @Override
    public void handleDeadLetterMessage(Order message) {
        System.out.println("【死信队列】Received order: " + message);
    }
}
