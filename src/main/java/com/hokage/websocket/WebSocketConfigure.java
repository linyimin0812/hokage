package com.hokage.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author yiminlin
 */
@Configuration
@EnableWebSocket
public class WebSocketConfigure implements WebSocketConfigurer {

    private WebSocketHandler webSocketHandler;

    @Autowired
    public void setWebSocketHandler(WebSocketHandler handler) {
        this.webSocketHandler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/ssh")
                .setAllowedOrigins("*");
    }
}
