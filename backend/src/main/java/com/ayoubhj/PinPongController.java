package com.ayoubhj;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PinPongController {

    record PingPong(String result){}

    @GetMapping("ping")
    public PingPong getPingPong(){
        return new PingPong("ayoub");
    }
}
