package com.everest.chatserver.controller;

import com.everest.chatserver.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

//Where Users are stored.
//Disconnect and then resend message on connecting.
@Controller
public class ChatController {
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public Message sendMessage(@Payload Message chatMessage) {
        if (chatMessage.getReceiver() == null || chatMessage.getReceiver().equals("")) {
            System.out.println("group message");
            messagingTemplate.convertAndSend("/topic", chatMessage);

        } else {
            System.out.println("personal message");
            messagingTemplate.convertAndSend("/topic/" + chatMessage.getReceiver(), chatMessage);

        }

        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic")
    public Message addUser(@Payload Message chatMessage,
                           SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }


}
