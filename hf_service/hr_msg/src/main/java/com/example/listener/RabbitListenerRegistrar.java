package com.example.listener;

import com.example.Properties.QueueConfig;
import com.example.Properties.RabbitQueuesProperties;
import com.example.consumer.QueueConsumer;
import jakarta.annotation.Resource;
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
        for (QueueConfig cfg : queuesProperties.getQueues()) {
            // 找到实现了 QueueConsumer 接口且对应队列的 Bean
            String qName = cfg.getName();
            Map<String, QueueConsumer> consumers = context.getBeansOfType(QueueConsumer.class);
            for (QueueConsumer<?> consumer : consumers.values()) {
                if (qName.equals(consumer.getQueueName())) {
                    // 创建监听容器
                    SimpleMessageListenerContainer container =
                            new SimpleMessageListenerContainer(connectionFactory);
                    container.setQueueNames(qName);
                    container.setConcurrentConsumers(cfg.getConcurrency());
                    container.setMaxConcurrentConsumers(cfg.getMaxConcurrency());
                    container.setAcknowledgeMode(AcknowledgeMode.AUTO);
                    // 将消费者封装为 MessageListenerAdapter 以支持 JSON 转换
                    MessageListenerAdapter adapter = new MessageListenerAdapter(consumer, "handleMessage");
                    adapter.setMessageConverter(messageConverter);
                    container.setMessageListener(adapter);
                    container.start();  // 启动容器
                }
            }
        }
    }
}

