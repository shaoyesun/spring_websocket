package com.controller.websocket;

import org.apache.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 16-10-26.
 */
public class WebSocketHander implements WebSocketHandler {

    private static Logger log = Logger.getLogger(WebSocketHander.class);
    private static int count = 0;//统计建立管道数
    private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();
    private static final Map<String, String> map = new HashMap();

    //初次链接成功执行
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        users.add(session);
        String key = (String) session.getAttributes().get("websocket_index");
        log.debug(key + " 链接成功......");
        if (key != null) {
            //未读消息处理逻辑
            count++;
            map.put(key, session.getId());
            session.sendMessage(new TextMessage(count + ""));
        }
    }

    //接受消息处理消息
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        String index = webSocketSession.getAttributes().get("websocket_index").toString();
        sendMessageToUsers1(index.split("_")[0], new TextMessage(webSocketMessage.getPayload() + ""));
        //sendMessageToUser(index, new TextMessage(webSocketMessage.getPayload() + ""));
    }

    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        count--;
        log.debug(webSocketSession.getAttributes().get("websocket_index") + " 链接出错，关闭链接......");
        users.remove(webSocketSession);
    }

    //关闭或离开此页面管道关闭
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        count--;
        log.debug(webSocketSession.getAttributes().get("websocket_index") + " 链接关闭......" + closeStatus.toString());
        users.remove(webSocketSession);
    }

    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        ArrayList<WebSocketSession> u = users;
        for (WebSocketSession user : users) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToUsers1(String index, TextMessage message) {
        ArrayList<WebSocketSession> u = users;
        Map<String, String> m = map;
        for (WebSocketSession user : users) {
            try {
                String[] str = user.getAttributes().get("websocket_index").toString().split("_");
                if (user.isOpen() && str[0].equals(index)) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userName, TextMessage message) {
        ArrayList<WebSocketSession> u = users;
        for (WebSocketSession user : users) {
            if (user.getAttributes().get("websocket_index").equals(userName)) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void sendMessageToUser1(String index, TextMessage message) {
        ArrayList<WebSocketSession> u = users;
        Map<String, String> m = map;
        for (WebSocketSession user : users) {
            if (user.getId().equals(map.get(index))) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static Map<String, String> getMap() {
        return map;
    }
}
