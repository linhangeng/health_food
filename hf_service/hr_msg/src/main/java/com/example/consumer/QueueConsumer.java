package com.example.consumer;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/21 22:17
 */
public interface QueueConsumer<T> {

    // 返回绑定的队列名
    String getQueueName();

    // 处理消息
    void handleMessage(T message);

    // 指定消息类型，用于反序列化
    Class<T> getMessageType();
}

