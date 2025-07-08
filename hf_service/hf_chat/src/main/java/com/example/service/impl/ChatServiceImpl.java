package com.example.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.example.model.dto.ChatDTO;
import com.example.service.ChatService;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


/**
 * @ClassName ChatServiceImpl
 * @Author sheng.lin
 * @Date 2025/7/7
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    @Qualifier(value = "openAiChatModel")
    OpenAiChatModel openAiChatModel;

    @Resource
    @Qualifier(value = "ds")
    DeepSeekChatModel ds;

    @Resource
    @Qualifier(value = "dsStream")
    DeepSeekChatModel dsStream;


    @Resource
    ChatMemory chatMemory;


    /**
     * 聊天
     *
     * @param chatDto
     * @Author sheng.lin
     * @Date 2025/7/7
     * @return: java.lang.String
     * @Version 1.0
     * @修改记录
     */
    @Override
    public ChatResponse chat(ChatDTO chatDto) {
        buildChatMemory(chatDto);
        // todo 构建对话参数
        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(1.00)
                .build();
        Prompt prompt = Prompt.builder()
                .chatOptions(chatOptions)
                .messages(getChatMemory(chatDto))
                .build();
        ChatResponse chatResponse = ds.call(prompt);
        saveChatMemory(chatDto, chatResponse);
        return chatResponse;
    }


    /**
     * 流式聊天
     *
     * @param chatDto
     * @Author sheng.lin
     * @Date 2025/7/7
     * @return: reactor.core.publisher.Flux<java.lang.String>
     * @Version 1.0
     * @修改记录
     */
    @Override
    public SseEmitter chatStream(ChatDTO chatDto) {
        // 创建 SseEmitter，设置超时时间为 100 秒
        SseEmitter sseEmitter = new SseEmitter(100000L);
        // 构建对话记忆
        buildChatMemory(chatDto);
        // 构建对话参数
        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(1.00)
                .build();
        Prompt prompt = Prompt.builder()
                .chatOptions(chatOptions)
                .messages(getChatMemory(chatDto))
                .build();
        // 订阅流式响
        Disposable subscription = dsStream.stream(prompt)
                .doOnNext(response -> {
                    try {
                        sseEmitter.send(SseEmitter.event()
                                .id(UUID.randomUUID().toString())
                                .name("ASSISTANT")
                                .data(response));
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                })
                .doOnError(sseEmitter::completeWithError)
                .doOnComplete(sseEmitter::complete)
                .subscribe();

        // 设置 SSE 客户端断开连接时的处理逻辑
        sseEmitter.onCompletion(subscription::dispose);
        sseEmitter.onTimeout(subscription::dispose);
        sseEmitter.onError(RuntimeException::new);
        return sseEmitter;
    }


    /**
     * 初始化会话记录
     *
     * @param chatDto
     * @Author sheng.lin
     * @Date 2025/7/7
     * @return: void
     * @Version 1.0
     * @修改记录
     */
    private void buildChatMemory(ChatDTO chatDto) {
        if (ObjectUtil.isNotEmpty(chatDto.getSessionId())) {
            chatMemory.add(chatDto.getSessionId(),
                    UserMessage.builder().text(chatDto.getContent()).build());
        }
    }


    /**
     * 获取会话记录
     *
     * @param chatDto
     * @Author sheng.lin
     * @Date 2025/7/8
     * @return: java.util.List<org.springframework.ai.chat.messages.Message>
     * @Version 1.0
     * @修改记录
     */
    private List<Message> getChatMemory(ChatDTO chatDto) {
        if (ObjectUtil.isNotEmpty(chatDto.getSessionId())) {
            return chatMemory.get(chatDto.getSessionId());
        }
        return null;
    }


    /**
     * 保存会话记录
     *
     * @param chatDto
     * @param chatResponse
     * @Author sheng.lin
     * @Date 2025/7/8
     * @return: void
     * @Version 1.0
     * @修改记录
     */
    private void saveChatMemory(ChatDTO chatDto, ChatResponse chatResponse) {
        chatMemory.add(chatDto.getSessionId(), SystemMessage.builder().text(chatResponse.getResult().getOutput().getText()).build());
    }
}
