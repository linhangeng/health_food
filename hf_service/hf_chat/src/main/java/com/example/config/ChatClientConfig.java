package com.example.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ChatClientConfig
 * @Author sheng.lin
 * @Date 2025/7/7
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Configuration
public class ChatClientConfig {



    

    /**
     * ds配置
     * @param
     * @Author sheng.lin
     * @Date 2025/7/7
     * @return: org.springframework.ai.deepseek.DeepSeekChatModel
     * @Version  1.0
     * @修改记录
     */
    @Bean
    @Qualifier(value = "ds")
    public DeepSeekChatModel ds(){
        return DeepSeekChatModel.builder()
                .deepSeekApi(DeepSeekApi.builder()
                        .baseUrl("https://api.deepseek.com/")
                        .apiKey("sk-a16f7b1e7d464a2facf1aaa3f50dfc06")
                        .build())
                .defaultOptions(DeepSeekChatOptions.builder()
                        // 其他配置
                        .model("deepseek-chat")
                        .build())
                .build();
    }


    /**
     * ds流式
     * @param
     * @Author sheng.lin
     * @Date 2025/7/7
     * @return: org.springframework.ai.deepseek.DeepSeekChatModel
     * @Version  1.0
     * @修改记录
     */
    @Bean
    @Qualifier(value = "dsStream")
    public DeepSeekChatModel dsStream(){
        return DeepSeekChatModel.builder()
                .deepSeekApi(DeepSeekApi.builder()
                        .baseUrl("https://api.deepseek.com/")
                        .apiKey("sk-a16f7b1e7d464a2facf1aaa3f50dfc06")
                        .build())
                .defaultOptions(DeepSeekChatOptions.builder()
                        // 其他配置
                        .model("deepseek-reasoner")
                        .build())
                .build();
    }



    /**
     * 上下文
     * @param
     * @Author sheng.lin
     * @Date 2025/7/7
     * @return: org.springframework.ai.chat.memory.ChatMemory
     * @Version  1.0
     * @修改记录
     */
    @Bean
    public ChatMemory chatMemory(){
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
    }


}
