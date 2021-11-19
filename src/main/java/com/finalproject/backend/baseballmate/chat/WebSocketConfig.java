package com.finalproject.backend.baseballmate.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독하고 있는 클라이언트에게 메시지 전달
        registry.enableSimpleBroker("/sub");
        // 서버에서 클라이언트로부터 메시지를 받을 api의 prefix
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //allowed origin 수정예정
        //웹소켓 연결 url
        registry.addEndpoint("/chatting")
                .setAllowedOriginPatterns("*")
                .withSockJS().setHeartbeatTime(25000);
    }

    @Override
    // 메세지를 받았을때 최초에 stompHandler 가 인터셉트 하도록 설정
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);

    }
}
