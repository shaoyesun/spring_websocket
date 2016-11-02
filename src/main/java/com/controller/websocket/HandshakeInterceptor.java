package com.controller.websocket;

import com.entity.User;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;

import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * Created by root on 16-10-26.
 */
public class HandshakeInterceptor implements org.springframework.web.socket.server.HandshakeInterceptor {

    //进入hander之前的拦截
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        String[] uri = request.getURI().toString().split("/");
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(true);
            //保证地址栏的请求和websocket的请求地址统一就能获取到了
            User user = (User) session.getAttribute("now_user");
            if (session != null) {
                //使用userName区分WebSocketHandler，以便定向发送消息
                map.put("websocket_index", uri[uri.length - 1] + "_" + user.getUserName());
            }
        }
        return true;
    }

    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception e) {

    }
}
