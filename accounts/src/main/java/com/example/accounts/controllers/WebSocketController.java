package com.example.accounts.controllers;

import com.example.accounts.models.Account;
import com.example.accounts.models.WebSocketAccount;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/message")
    @SendTo("/topic/accounts")
    public WebSocketAccount sendMessage(WebSocketAccount account) {
        return account;
    }
}
