package com.example.consumer.impl;


import com.example.constants.RabbitMQConstants;
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


    /**
     * 获取队列名称
     * @param
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: java.lang.String
     * @Version  1.0
     * @修改记录
     */
    @Override
    public String getQueueName() {
        return RabbitMQConstants.ORDER_QUEUE;
    }

    /**
     * 消息类型
     * @param
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: java.lang.Class<com.example.model.dto.Order>
     * @Version  1.0
     * @修改记录
     */
    @Override
    public Class<Order> getMessageType() {
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
    public void handleMessage(Order message) {
        // 处理订单消息
        System.out.println("【普通队列】Received order: " + message);
        try {
            // 模拟耗时操作（休眠3秒）
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("处理被中断: " + e.getMessage());
        }
        System.out.println("【普通队列】订单处理完成: " + message);
    }
}

