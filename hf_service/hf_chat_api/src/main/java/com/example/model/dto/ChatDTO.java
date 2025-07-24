package com.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ChatDTO
 * @Author sheng.lin
 * @Date 2025/7/7
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    /**
     * 会话id
     */
    private String sessionId;

    /**
     * 提问内容
     */
    private String content;
}
