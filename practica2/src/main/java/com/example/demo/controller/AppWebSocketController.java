package com.example.demo.controller;

import com.example.demo.config.websocket.OutputMessage;
import com.example.demo.dto.Message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class AppWebSocketController {

    @GetMapping("/mensajes")
    public  String mensajes(ModelMap interfazConPantalla){
        return "mensajes";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message){
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(),time);
    }
}
