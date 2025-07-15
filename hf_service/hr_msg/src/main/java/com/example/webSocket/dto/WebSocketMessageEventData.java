package com.example.webSocket.dto;

import cn.hutool.json.JSONObject;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

/**
 * @ClassName WebSocketMessageEventData
 * @Author sheng.lin
 * @Date 2025/7/15
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 * WebSocket 消息事件数据
 */
@Data
public class WebSocketMessageEventData {

    /**
     * 消息类型
     */
    private String type;

    /**
     * 解析后的 JSON 对象
     */
    private JSONObject parsedMessage;

    /**
     * WebSocket 会话
     */
    private WebSocketSession session;

    public WebSocketMessageEventData(String type, JSONObject parsedMessage, WebSocketSession session) {
        this.type = type;
        this.parsedMessage = parsedMessage;
        this.session = session;
    }
}

