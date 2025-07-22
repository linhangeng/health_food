package com.example.constants;

/**
 * @ClassName RabbitMQConstants
 * @Author sheng.lin
 * @Date 2025/7/22
 * @Version 1.0
 * @修改记录
 **/
public class RabbitMQConstants {

    /**
     * 订单(普通队列)
     */
    public static final String ORDER_QUEUE = "order.queue";

    /**
     * 订单(死信队列)
     */
    public static final String ORDER_DLQ = "order.queue.dlq";
}
