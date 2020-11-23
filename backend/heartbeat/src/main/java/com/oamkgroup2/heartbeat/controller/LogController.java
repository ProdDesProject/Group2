package com.oamkgroup2.heartbeat.controller;

import java.util.List;

import com.oamkgroup2.heartbeat.service.LogService;
import com.oamkgroup2.heartbeat.model.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/getall")
    public List<Log> getAll() {
        return logService.getAll();
    }

    @PostMapping("new/log")
    public Log newLog(@RequestBody String log) {
        return logService.newLog(log);
    }

}
