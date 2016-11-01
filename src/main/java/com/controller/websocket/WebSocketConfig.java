package com.controller.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 用于页面与后端管道的建立 Created by root on 16-10-26.
 */
@Configuration
@EnableWebSocket//开启websocket
public class WebSocketConfig implements WebSocketConfigurer {
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHander(), "/echo").addInterceptors(new HandshakeInterceptor()); //支持websocket 的访问链接
        registry.addHandler(new WebSocketHander(), "/sockjs/echo").addInterceptors(new HandshakeInterceptor()).withSockJS(); //不支持websocket的访问链接
    }
}
