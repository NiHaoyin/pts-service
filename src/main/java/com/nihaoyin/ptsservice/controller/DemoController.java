package com.nihaoyin.ptsservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;

@RestController
public class DemoController {
    WebSocketServer webSocketServer = new WebSocketServer();

    @GetMapping("index")
    public ResponseEntity<String> index(){
        return ResponseEntity.ok("请求成功");
    }

    @GetMapping("page")
    public ModelAndView page(){
        return new ModelAndView("websocket");
    }

    @RequestMapping("/pushtrace")
    public ResponseEntity<String> pushToWeb(String message, @PathVariable String toUserId) throws IOException {
        webSocketServer.sendMessage(message);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }

}
