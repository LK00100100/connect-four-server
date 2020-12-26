package com.lk00100100.connectfourserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Sets up the web sockets.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/con4");   //prepends /con4 before every incoming message destination
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //if websocket isn't available, use other available methods
        registry.addEndpoint("/con4-ws")
                .setAllowedOrigins("http://localhost:8081", "http://127.0.0.1:8081")
                .withSockJS();
    }

}
