package com.example.listener;

import com.example.Properties.QueueConfig;
import com.example.Properties.RabbitQueuesProperties;
import com.example.consumer.DeadLetterQueueConsumer;
import com.example.consumer.QueueConsumer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/21 22:18
 * rabbitmq监听器
 */
@Component
@Slf4j
public class RabbitListenerRegistrar implements ApplicationRunner {

    @Resource
    ConnectionFactory connectionFactory;
    @Resource
    Jackson2JsonMessageConverter messageConverter;
    @Resource
    ApplicationContext context;
    @Resource
    RabbitQueuesProperties queuesProperties;


    @Override
    public void run(ApplicationArguments args) {
        // 1. 处理普通队列的监听
        registerNormalQueueListeners();
        // 2. 处理死信队列的监听（新增逻辑）
        registerDeadLetterQueueListeners();
    }


    /**
     * 注册普通队列的监听器（原逻辑保留）
     */
    private void registerNormalQueueListeners() {
        for (QueueConfig cfg : queuesProperties.getQueues()) {
            String queueName = cfg.getName();
            // 查找对应普通队列的消费者
            Map<String, QueueConsumer> normalConsumers = context.getBeansOfType(QueueConsumer.class);
            for (QueueConsumer<?> consumer : normalConsumers.values()) {
                if (queueName.equals(consumer.getQueueName())) {
                    createListenerContainer(queueName, cfg, consumer, "handleMessage");
                    break;
                }
            }
        }
    }


    /**
     * 注册死信队列的监听器（新增逻辑）
     */
    private void registerDeadLetterQueueListeners() {
        for (QueueConfig cfg : queuesProperties.getQueues()) {
            // 死信队列命名规则：原队列名 + ".dlq"
            String dlqName = cfg.getName() + ".dlq";
            // 查找对应死信队列的消费者（实现DeadLetterQueueConsumer接口）
            Map<String, DeadLetterQueueConsumer> dlqConsumers = context.getBeansOfType(DeadLetterQueueConsumer.class);
            for (DeadLetterQueueConsumer<?> consumer : dlqConsumers.values()) {
                if (dlqName.equals(consumer.getDeadLetterQueueName())) {
                    // 为死信队列创建监听器容器，复用原队列的并发配置（也可单独配置）
                    createListenerContainer(dlqName, cfg, consumer, "handleDeadLetterMessage");
                    break;
                }
            }
        }
    }


    /**
     * 通用创建监听器容器的方法（复用逻辑，减少重复代码）
     * @param queueName 队列名（普通队列或死信队列）
     * @param cfg 队列配置（包含并发数等）
     * @param consumer 消费者实例（普通消费者或死信消费者）
     * @param handleMethod 消费方法名（普通消息：handleMessage；死信消息：handleDeadLetterMessage）
     */
    private void createListenerContainer(String queueName, QueueConfig cfg, Object consumer, String handleMethod) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        // 监听的队列名（普通队列或死信队列）
        container.setQueueNames(queueName);
        // 并发消费者数量
        container.setConcurrentConsumers(cfg.getConcurrency());
        // 最大并发数
        container.setMaxConcurrentConsumers(cfg.getMaxConcurrency());
        // 确认模式（与普通队列保持一致）
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        // 绑定消费者和消息处理方法
        MessageListenerAdapter adapter = new MessageListenerAdapter(consumer, handleMethod);
        // 复用JSON消息转换器
        adapter.setMessageConverter(messageConverter);
        container.setMessageListener(adapter);
        // 启动监听器容器
        container.start();
        log.info("已启动监听器：队列:{}，消费者:{}",queueName,consumer.getClass().getSimpleName());
    }
}

