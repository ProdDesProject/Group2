package com.oamkgroup2.heartbeat.controller;

import java.util.List;

import com.oamkgroup2.heartbeat.service.LogService;
import com.oamkgroup2.heartbeat.model.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API controller for handling requests about logs.
 */
@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * Get all logs. Only for testing purposes
     */
    @GetMapping("/getall")
    public List<Log> getAll() {
        return logService.getAll();
    }

    /**
     * Persist a new log in the database. //TODO: check if ID is needed.
     * 
     * @param log JSON representation of a Log object.
     */
    @PostMapping("new/log")
    public Log newLog(@RequestBody String log) {
        return logService.newLog(log);
    }

    /**
     * Persist a batch of logs.
     * 
     * @param logs a JSON representation of an array of logs.
     */
    @PostMapping("new/batch")
    public Log[] newBatch(@RequestBody String logs) {
        return logService.newBatch(logs);
    }

}
