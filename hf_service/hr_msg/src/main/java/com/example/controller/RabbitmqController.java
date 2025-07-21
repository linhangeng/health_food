package com.example.controller;


import com.example.model.dto.Order;
import com.example.producer.RabbitProducer;
import com.example.protocol.ApiServiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linhangeng
 * @version 1.0
 * @description: TODO
 * @date 2025/7/21 22:36
 */
@RestController
@Tag(name = "rabbitmq", description = "rabbitmq管理")
public class RabbitmqController {

    @Resource
    RabbitProducer rabbitProducer;

    @GetMapping("/sendMessageByMQ")
    @Operation(summary = "用MQ发送消息")
    public ApiServiceResponse<String> sendMessageByMQ() {
        rabbitProducer.send("", "order.queue", new Order("1","测试"));
        return ApiServiceResponse.success();

    }
}
