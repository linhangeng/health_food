package com.example.webSocket.ws;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.webSocket.config.WebSocketProperties;
import com.example.webSocket.dto.WebSocketMessageEventData;
import com.example.webSocket.event.WebSocketMessageEvent;
import com.example.webSocket.send.WebSocketMessageSender;
import com.example.webSocket.session.WebSocketSessionManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriTemplate;

import java.io.EOFException;
import java.net.URI;
import java.util.Map;

/**
 * 自定义参数发送信息的WebSocket
 */
@Slf4j
public class CustomParamWebSocketHandler extends TextWebSocketHandler implements ApplicationContextAware {

    @Getter
    private final WebSocketSessionManager sessionManager;

    @Getter
    private final WebSocketMessageSender sender;

    private final WebSocketProperties properties;

    @Getter
    private final String uriTemplate;

    @Getter
    private final String urlPath;

    /**
     * 用于标识 WebSocket 连接的主键参数名
     * 默认为"param"，可以通过构造函数自定义
     */
    @Getter
    private final String paramKey;

    /**
     * Spring 事件发布器，用于发布 WebSocket 消息事件
     */
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        eventPublisher = applicationContext;
    }


    /**
     * 构造函数
     *
     * @param properties WebSocket属性配置
     */
    public CustomParamWebSocketHandler(WebSocketProperties properties) {
        this(properties, "/websocket/*", "/websocket/{param}", "param");
    }

    /**
     * 构造函数
     *
     * @param properties  WebSocket属性配置
     * @param urlPath     URL路径模式，例如"/websocket/*"
     * @param uriTemplate URI模板，例如"/websocket/{param}
     * @param paramKey    用于标识 WebSocket 连接的主键参数名
     */
    public CustomParamWebSocketHandler(WebSocketProperties properties, String urlPath, String uriTemplate, String paramKey) {
        this.sessionManager = new WebSocketSessionManager(uriTemplate);
        this.sender = new WebSocketMessageSender(sessionManager);
        this.properties = properties;
        this.uriTemplate = uriTemplate;
        this.urlPath = urlPath;
        this.paramKey = paramKey;
    }


    /**
     * 连接消息
     * @param session
     * @Author sheng.lin
     * @Date 2025/7/15
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        String pathVariable = getPathVariable(session);
        if (StrUtil.isNotBlank(pathVariable)) {
            // 实现 session 支持并发，可参考 https://blog.csdn.net/abu935009066/article/details/131218149
            session = new ConcurrentWebSocketSessionDecorator(session,
                    properties.getSendTimeLimit(),
                    properties.getBufferSizeLimit());
            sessionManager.add(pathVariable, session);
            log.info("{}, {} 建立连接, 该Key连接总数: {}, 系统连接总数: {}", uriTemplate, pathVariable,
                    sessionManager.getSession(pathVariable).size(),
                    sessionManager.getAllSession().size());
        }
    }


    /**
     * 获取路径变量映射
     *
     * @param session WebSocket会话
     * @return 路径变量映射
     */
    protected String getPathVariable(WebSocketSession session){
        final URI uri = session.getUri();
        if (uri == null || uri.getPath() == null) {
            log.error("获取 websocket url 失败");
            return null;
        }
        UriTemplate template = new UriTemplate(uriTemplate);
        Map<String, String> pathVariables = template.match(uri.getPath());

        return pathVariables.getOrDefault(paramKey, "");
    }


    /**
     * 处理接收到的参数
     * @param session
     * @param message
     * @Author sheng.lin
     * @Date 2025/7/15
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
        String pathVariable = getPathVariable(session);
        if (StrUtil.isBlank(pathVariable)) {
            return;
        }
        String messagePayload = message.getPayload();
        log.info("来自客户端的消息->{}", messagePayload);
        if (!JSONUtil.isTypeJSON(messagePayload)) {
            sender.sendToParam(pathVariable, "ok");
            return;
        }
        // 解析为 JSON
        JSONObject jsonMessage = JSONUtil.parseObj(messagePayload);
        // 检查是否包含 type 字段
        if (ObjectUtil.isNotEmpty(jsonMessage) && jsonMessage.containsKey("type")) {
            String messageType = jsonMessage.getStr("type");
            // 异步发布事件
            if (eventPublisher != null) {
                var eventData = new WebSocketMessageEventData(messageType, jsonMessage, session);
                // 发布事件，避免阻塞 WebSocket 消息处理
                eventPublisher.publishEvent(new WebSocketMessageEvent(eventData));
                log.info("WebSocket 消息事件已发布: paramKey={}, type={}", pathVariable, messageType);
            }
        } else {
            sender.sendToParam(pathVariable, "ok");
        }
    }


    /**
     * 处理错误
     * @param session
     * @param exception
     * @Author sheng.lin
     * @Date 2025/7/15
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        if (!(exception instanceof EOFException)) {
            log.error("WebSocket 连接发生错误", exception);
        }
    }


    /**
     * 处理连接关闭
     * @param session
     * @param closeStatus
     * @Author sheng.lin
     * @Date 2025/7/15
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)  {
        String primaryKey = getPathVariable(session);
        if (StrUtil.isNotBlank(primaryKey)) {
            sessionManager.remove(primaryKey, session);
        }
        log.info("{}, {} 关闭连接, 该Key连接总数: {}, 系统连接总数: {}", uriTemplate, primaryKey,
                sessionManager.getSession(primaryKey).size(),
                sessionManager.getAllSession().size());
    }
}
