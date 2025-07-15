package com.example.webSocket.event;

import cn.hutool.json.JSONObject;
import com.example.webSocket.dto.WebSocketMessageEventData;
import lombok.Data;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.WebSocketSession;

/**
 * @ClassName WebSocketMessageEvent
 * @Author sheng.lin
 * @Date 2025/7/14
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */

/**
 * WebSocket 消息事件
 * 当 WebSocket 接收到包含 type 字段的 JSON 消息时触发此事件
 *
 * @see #verify(String)
 */
public class WebSocketMessageEvent extends ApplicationEvent {

    public WebSocketMessageEvent(WebSocketMessageEventData source) {
        super(source);
    }

    /**
     * 获取事件数据
     */
    public WebSocketMessageEventData getData() {
        return (WebSocketMessageEventData) getSource();
    }

    /**
     * 校验是不是需要处理的类型
     */
    public boolean verify(String type) {
        if (type == null) {
            return false;
        }
        return type.equals(getData().getType());
    }
}

