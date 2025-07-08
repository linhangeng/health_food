package com.example.model.dto;

import lombok.Data;

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
