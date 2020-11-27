package com.oamkgroup2.heartbeat.controller;

import java.util.List;

import javax.validation.Valid;

import com.oamkgroup2.heartbeat.service.LogService;
import com.oamkgroup2.heartbeat.model.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API controller for handling requests about logs.
 */
@Validated
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
    public Log newLog(@Valid @RequestBody Log log) {
        return logService.newLog(log);
    }

    /**
     * Persist a batch of logs.
     * 
     * @param logs a JSON representation of an array of logs.
     */
    @PostMapping("new/batch")
    public Log[] newBatch(@Valid @RequestBody Log[] logs) {
        return logService.newBatch(logs);
    }

}
