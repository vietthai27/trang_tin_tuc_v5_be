package com.thai27.trang_tin_tuc_v5_be.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greeting")
    public String hello(@Payload String name) {
        return "Hello " + name;
    }
}
