package com.aethernet.chessbackend.chat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ChatMessage {

    private String content;
    private String sender;
    private MessageType messageType;

}
