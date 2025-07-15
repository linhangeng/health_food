package com.example.webSocket.send;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.webSocket.config.WebSocketProperties;
import com.example.util.ThreadPoolUtils;
import com.example.webSocket.dto.WsMessage;
import com.example.webSocket.session.WebSocketSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.SessionLimitExceededException;

import java.io.IOException;
import java.util.Collection;

/**
 * @ClassName WebSocketMessageSender
 * @Author sheng.lin
 * @Date 2025/7/14
 * @Version 1.0
 * @修改记录
 * 消息发送组件
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */

@Slf4j
public class WebSocketMessageSender {

    /**
     * 共用一个线程池
     */
    private static ThreadPoolTaskExecutor executor;

    private final WebSocketSessionManager sessionManager;

    public WebSocketMessageSender(WebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public static synchronized void initializeExecutor(WebSocketProperties properties) {
        if (executor != null) {
            return;
        }
        executor = ThreadPoolUtils.createThreadPoolTaskExecutor(
                "websocket-sender",
                properties.getCoreThreadSize(),
                properties.getMaxThreadSize(),
                properties.getQueueCapacity()
        );
        log.info("初始化 WebSocketMessageSender 线程池成功");
    }

    /**
     * 发送消息到客户端
     *
     * @param param   分组
     * @param message 发送的消息
     */
    public void sendToParam(String param, WsMessage<?> message) {
        sendToParam(param, JSONUtil.toJsonStr(message));
    }

    /**
     * 广播消息到全部客户端
     *
     * @param message 发送的消息
     */
    public void sendToAll(String message) {
        for (String key : sessionManager.getAllKeys()) {
            sendToParam(key, message);
        }
    }

    /**
     * 广播消息到全部客户端
     *
     * @param message 发送的消息
     */
    public void sendToAll(WsMessage<?> message) {
        for (String key : sessionManager.getAllKeys()) {
            sendToParam(key, message);
        }
    }

    /**
     * 发送消息到某个连接
     *
     * @param session websocket连接
     * @param message 发送的消息
     */
    public static void sendToSession(WebSocketSession session, WsMessage<?> message) {
        executor.execute(() -> {
            // 1. 各种校验，保证 Session 可以被发送
            if (session == null || !session.isOpen()) {
                return;
            }
            // 2. 执行发送
            try {
                session.sendMessage(new TextMessage(JSONUtil.toJsonStr((message))));
            } catch (IOException ex) {
                log.error(StrUtil.format("给[{}]发送消息失败", session.getId()), ex);
            } catch (SessionLimitExceededException ex) {
                // 一旦有一条消息发送超时，或者发送数据大于限制，limitExceeded 标志位就会被设置成true，标志这这个 session 被关闭
                // 后面的发送调用都是直接返回不处理，但只是被标记为关闭连接本身可能实际上并没有关闭，这是一个坑需要注意。
                try {
                    session.close();
                } catch (IOException e) {
                    log.error(StrUtil.format("主动关闭[{}]连接失败", session.getId()), e);
                }
                log.error(StrUtil.format("给[[{}]发送消息失败", session.getId()), ex);
            }
        });
    }

    /**
     * 发送消息到某个参数的客户端
     *
     * @param param   websocket连接时的参数
     * @param message 发送的消息
     */
    public void sendToParam(String param, String message) {
//        executor.execute(() -> {
            // 1. 获得 Session 列表
            Collection<WebSocketSession> sessions = sessionManager.getSession(param);
            if (CollUtil.isEmpty(sessions)) {
                return;
            }
            // 2. 执行发送
            sessions.forEach(session -> {
                // 1. 各种校验，保证 Session 可以被发送
                if (session == null || !session.isOpen()) {
                    sessionManager.remove(param, session);
                    return;
                }
                // 2. 执行发送
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException ex) {
                    log.error(StrUtil.format("给[{}]分组的[{}]发送消息失败", param, session.getId()), ex);
                } catch (SessionLimitExceededException ex) {
                    // 一旦有一条消息发送超时，或者发送数据大于限制，limitExceeded 标志位就会被设置成true，标志这这个 session 被关闭
                    // 后面的发送调用都是直接返回不处理，但只是被标记为关闭连接本身可能实际上并没有关闭，这是一个坑需要注意。
                    try {
                        session.close();
                        sessionManager.remove(param, session);
                    } catch (IOException e) {
                        log.error(StrUtil.format("主动关闭[{}]分组的[{}]连接失败", param, session.getId()), e);
                    }
                    log.error(StrUtil.format("给[{}]分组的[{}]发送消息失败", param, session.getId()), ex);
                }
            });
//        });
    }

}

