package com.example.service.impl;

import com.example.service.SendUserService;
import com.example.webSocket.ws.CustomParamWebSocketHandler;
import com.example.webSocket.dto.WsMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SendUserServiceImpl
 * @Author sheng.lin
 * @Date 2025/7/15
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Service
public class SendUserServiceImpl implements SendUserService {


    @Resource(name = "userWebSocketHandler")
    CustomParamWebSocketHandler customParamWebSocketHandler;


    /**
     * 发送消息给特定的userId
     * @param userId
     * @param msg
     * @Author sheng.lin
     * @Date 2025/7/15
     * @return: java.lang.Integer
     * @Version  1.0
     * @修改记录
     */
    @Override
    public String sendToUser(String userId, String msg) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "系统通知");
        payload.put("content", msg);
        WsMessage<Map<String, Object>> message = WsMessage.success(payload);
        customParamWebSocketHandler.getSender().sendToParam(userId,message);
        return message.getMessage();
    }
}
