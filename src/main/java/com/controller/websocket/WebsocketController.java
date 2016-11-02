package com.controller.websocket;

import com.entity.User;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;

/**
 * websocket数据推送测试 Created by root on 16-10-27.
 */
@Controller
@RequestMapping(value = "/websocket")
public class WebsocketController {

    private static Logger log = Logger.getLogger(WebsocketController.class);

    @Bean
    public WebSocketHander webSocketHandler() {
        return new WebSocketHander();
    }

    /**
     * 后台推送消息给指定用户
     *
     * @param request
     * @return
     */
    @RequestMapping("/auditing")
    @ResponseBody
    public String auditing(HttpServletRequest request, String index) {
        User user = (User) request.getSession().getAttribute("now_user");
        //webSocketHandler().sendMessageToUser1(index + "_" + user.getUserName(), new TextMessage(user.getUserName()));
        webSocketHandler().sendMessageToUsers1(index, new TextMessage(user.getUserName()));
        return "success";
    }

    /**
     * 打开此页面前端和后端正式建立管道，关闭或离开此页面管道关闭
     *
     * @return
     */
    @RequestMapping(value = "/websocket")
    public String websocket() {
        return "websocket";
    }

    @RequestMapping(value = "/websocket1")
    public String websocket1() {
        return "websocket1";
    }

}
