package com.example.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {

    @GetMapping("/data")
    public String getData() {
        return "server-data";
    }
}
