package com.example.webSocket.session;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ClassName WebSocketSessionManager
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
 * WebSocket Session管理实现，不同的CustomParamWebSocketHandler之间WebSocketSessionManager不复用
 * <p>
 * 每个key是一个分组，每个key下支持多个客户端
 *
 */
@Slf4j
public class WebSocketSessionManager {

    /**
     * 全局管理器
     */
    public static Map<String, Map<String, CopyOnWriteArraySet<WebSocketSession>>> webSocketSessionManagers = new HashMap<>();

    /**
     * key 与 WebSocketSession 映射
     * value 为集合
     */
    private final ConcurrentMap<String, CopyOnWriteArraySet<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    public WebSocketSessionManager(String uriTemplate) {
        webSocketSessionManagers.put(uriTemplate, this.sessions);
    }


    /**
     * 添加 Session
     *
     * @param session Session
     */
    public void add(String key, WebSocketSession session) {
        // 使用compute方法来确保线程安全地添加会话
        sessions.compute(key, (k, v) -> {
            if (v == null) {
                v = new CopyOnWriteArraySet<>();
            }
            v.add(session);
            return v;
        });
    }

    /**
     * 移除 Session
     *
     * @param session Session
     */
    public void remove(String key, WebSocketSession session) {
        CopyOnWriteArraySet<WebSocketSession> webSocketSessions = sessions.get(key);
        if (CollUtil.isNotEmpty(webSocketSessions)) {
            webSocketSessions.removeIf(t -> t.getId().equals(session.getId()));
        }
    }

    /**
     * 移除 key 下的 所有 Session
     */
    public void remove(String key) {
        CopyOnWriteArraySet<WebSocketSession> sessionByKeys = sessions.get(key);
        if (CollUtil.isNotEmpty(sessionByKeys)) {
            synchronized (sessionByKeys) {
                for (WebSocketSession session : sessionByKeys) {
                    try {
                        session.close();
                    } catch (IOException e) {
                        log.error("关闭 {} 的 ws 连接失败", key);
                    }
                }
                sessions.remove(key);
            }
        }
    }

    /**
     * 获得指定 key 的 Session 列表
     *
     * @param key key
     * @return Session
     */
    public Collection<WebSocketSession> getSession(String key) {
        if (StrUtil.isEmpty(key)) {
            return Collections.emptyList();
        }
        return sessions.getOrDefault(key, new CopyOnWriteArraySet<>());
    }

    /**
     * 获取所有session
     */
    public Collection<WebSocketSession> getAllSession() {
        return sessions.values().stream().flatMap(Collection::stream).toList();
    }

    /**
     * 获取所有key
     */
    public Set<String> getAllKeys() {
        return sessions.keySet();
    }

}

