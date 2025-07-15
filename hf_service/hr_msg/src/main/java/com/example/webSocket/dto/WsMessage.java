package com.example.webSocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName WsMessage
 * @Author sheng.lin
 * @Date 2025/7/14
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 * 消息发送主体
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsMessage<T> {
    private String code;
    private String message;
    private T data;

    // 静态工厂方法
    public static <T> WsMessage<T> success(T data) {
        return new WsMessage<>("200", "成功", data);
    }

    public static <T> WsMessage<T> error(String message) {
        return new WsMessage<>("500", message, null);
    }
}
