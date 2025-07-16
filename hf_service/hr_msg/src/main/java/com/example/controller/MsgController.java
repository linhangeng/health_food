package com.example.controller;

import com.example.protocol.ServiceResponse;
import com.example.service.SendUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MsgController
 * @Author sheng.lin
 * @Date 2025/7/15
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@RestController
@Tag(name = "msg", description = "webSocket管理")
public class MsgController {


    @Resource
    SendUserService sendUserService;

    @GetMapping("/sendToUser")
    @Operation(summary = "发送消息到客户端")
    public ServiceResponse<String> sendToUser(@RequestParam("userId") String userId, @RequestParam("msg") String msg) {
        return new ServiceResponse<>(sendUserService.sendToUser(userId, msg));
    }
}
