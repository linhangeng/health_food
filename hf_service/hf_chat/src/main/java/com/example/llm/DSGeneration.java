package com.example.llm;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.model.ModelResult;
import org.springframework.ai.model.ResultMetadata;

import java.util.Objects;

/**
 * @ClassName DSGeneration
 * @Author sheng.lin
 * @Date 2025/7/10
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
public class DSGeneration implements ModelResult<DeepSeekAssistantMessage> {

    private final DeepSeekAssistantMessage deepSeekAssistantMessage;
    private ChatGenerationMetadata chatGenerationMetadata;

    public DSGeneration(DeepSeekAssistantMessage deepSeekAssistantMessage) {
        this(deepSeekAssistantMessage, ChatGenerationMetadata.NULL);
    }

    public DSGeneration(DeepSeekAssistantMessage deepSeekAssistantMessage, ChatGenerationMetadata chatGenerationMetadata) {
        this.deepSeekAssistantMessage = deepSeekAssistantMessage;
        this.chatGenerationMetadata = chatGenerationMetadata;
    }


    public DeepSeekAssistantMessage getOutput() {
        return this.deepSeekAssistantMessage;
    }

    public ChatGenerationMetadata getMetadata() {
        ChatGenerationMetadata chatGenerationMetadata = this.chatGenerationMetadata;
        return chatGenerationMetadata != null ? chatGenerationMetadata : ChatGenerationMetadata.NULL;
    }
}
