package com.spring.chatapplication.Chat.Application.using.Websocket.config;

import com.spring.chatapplication.Chat.Application.using.Websocket.chat.ChatMessage;
import com.spring.chatapplication.Chat.Application.using.Websocket.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RequiredArgsConstructor
@Component
@Slf4j
public class WebSocketEventListener {

    private SimpMessageSendingOperations messageTemplate;
    @EventListener
    public void handleWebSocketDisconnection(SessionDisconnectEvent event){

        StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(event.getMessage());

        String username= (String) headerAccessor.getSessionAttributes().get("username");

        if(username!=null){
            log.info("User disconnected:{}",username);
            var chatMessage= ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();
            messageTemplate.convertAndSend("/topic/public",chatMessage);
        }
    }
}
