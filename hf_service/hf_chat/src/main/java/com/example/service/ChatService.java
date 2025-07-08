package com.example.service;

import com.example.model.dto.ChatDTO;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

/**
 * @ClassName ChatService
 * @Author sheng.lin
 * @Date 2025/7/7
 * @Version 1.0
 * @修改记录
 **/
public interface ChatService {


    ChatResponse chat(ChatDTO chatDto);

    SseEmitter chatStream(ChatDTO chatDto);




}
