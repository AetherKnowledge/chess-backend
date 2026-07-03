package com.aethernet.chessbackend.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage
    ){
        log.info("Chat message received from {}: {}", chatMessage.getSender(), chatMessage.getContent());
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ){
        if (headerAccessor.getSessionAttributes() == null) {
            log.warn("No session attributes available for join event from {}", chatMessage.getSender());
            return chatMessage;
        }

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        log.info("User {} joined the chat", chatMessage.getSender());
        return chatMessage;
    }
}
