package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.Heartbeat;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class HeartbeatController {

    @GetMapping(value = "heartbeat", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Heartbeat> checkHeartbeat() {
        Heartbeat hb = new Heartbeat("UP", "Server is running");
        return new ResponseEntity<>(hb, HttpStatusCode.valueOf(200));
    }
}