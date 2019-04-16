package com.test.webservice;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

/**
 * 继承WebSocketHandler对象。
 */
public class SpringWebSocketHandler extends TextWebSocketHandler {

    private final static Map<String, WebSocketSession> users = new HashMap<>();

    /**
     * 连接成功时候，会触发页面上onopen方法
     */
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connect to the websocket success......当前数量:" + users.size());
    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> attrMap = session.getAttributes();
        String username = attrMap.get("WEBSOCKET_USERNAME").toString();
        users.put(username, session);
        System.out.println("user:-" + username + ":" + message.getPayload());
        TextMessage dataPushMsg = new TextMessage(username + ":" + message.getPayload());
        sendMessageToUsers(dataPushMsg);
    }

    /**
     * 关闭连接时触发
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("websocket connection closed......");
        String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        System.out.println("用户" + username + "已退出！");
        users.remove(session);
        System.out.println("剩余在线用户" + users.size());
    }

    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        System.out.println("websocket connection closed......");
        users.remove(session);
    }

    public boolean supportsPartialMessages() {
        return false;
    }


    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    private void sendMessageToUsers(TextMessage message) {
        for (Map.Entry entry : users.entrySet()) {
            {
                WebSocketSession user = (WebSocketSession) entry.getValue();
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
