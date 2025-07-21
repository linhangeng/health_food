package com.example.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/21 22:11
 */
@Data
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitQueuesProperties {
    private List<QueueConfig> queues;
}


