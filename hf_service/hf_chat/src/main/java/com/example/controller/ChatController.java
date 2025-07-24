package com.example.controller;

import com.example.model.dto.ChatDTO;
import com.example.model.dto.HealthyFoodSearchDTO;
import com.example.protocol.ApiServiceResponse;
import com.example.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @ClassName SearchController
 * @Author sheng.lin
 * @Date 2025/7/4
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@RestController
@RequestMapping("/openApi")
@Tag(name = "/openApi", description = "openApi聊天接口管理")
public class ChatController {

    @Resource
    ChatService chatService;


    @PostMapping("/chat")
    @Operation(summary = "聊天")
    public ApiServiceResponse<ChatResponse> chat(@RequestBody ChatDTO chatDto){
        return new ApiServiceResponse<>(chatService.chat(chatDto));
    }

    @PostMapping(value = "/chatStream",produces = MediaType.TEXT_EVENT_STREAM_VALUE )
    @Operation(summary = "流式聊天")
    public SseEmitter chatStream(@RequestBody ChatDTO chatDto){
        return chatService.chatStream(chatDto);
    }

}
