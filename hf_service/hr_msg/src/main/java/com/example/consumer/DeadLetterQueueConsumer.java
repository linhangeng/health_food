package com.example.consumer;

/**
 * @ClassName DeadLetterQueueConsumer
 * @Author sheng.lin
 * @Date 2025/7/22
 * @Version 1.0
 * @修改记录
 **/
/**
 * 死信队列消费者接口
 * 用于处理死信队列中的消息
 */
public interface DeadLetterQueueConsumer<T> {

    /**
     * 获取死信队列名称（需与配置的死信队列名一致：原队列名.dlq）
     */
    String getDeadLetterQueueName();

    /**
     * 指定消息类型，用于反序列化
     */
    Class<T> getDeadLetterMessageType();

    /**
     * 处理死信消息的方法
     */
    void handleDeadLetterMessage(T message);


}