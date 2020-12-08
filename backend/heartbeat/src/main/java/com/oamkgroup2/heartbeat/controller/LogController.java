package com.oamkgroup2.heartbeat.controller;

import java.util.List;

import javax.validation.Valid;

import com.oamkgroup2.heartbeat.service.LogService;
import com.oamkgroup2.heartbeat.exception.EntityNotFoundException;
import com.oamkgroup2.heartbeat.model.Log;
import com.oamkgroup2.heartbeat.model.LogDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Log>> getAll() {
        return new ResponseEntity<>(logService.getAll(), HttpStatus.OK);
    }

    /**
     * Get the latest +1 sleepsession. Usually called when a new 'sleep session' (or
     * night), is started, so the correct sleepsession can be set. Represents one
     * night's sleep, and is used to find all logs for one night.
     * 
     * @throws EntityNotFoundException
     */
    @GetMapping("/sleepsession/latest")
    public ResponseEntity<Long> getLatestSleepSession(@RequestBody Long userId) throws EntityNotFoundException {
        return new ResponseEntity<>(logService.getLatestSleepSession(userId), HttpStatus.OK);
    }

    /**
     * Persist a new log in the database.
     * 
     * @param logDTO JSON representation of a Log object.
     */
    @PostMapping("new/log")
    public ResponseEntity<Log> newLog(@Valid @RequestBody LogDTO logDTO) {
        return new ResponseEntity<>(logService.newLog(logDTO), HttpStatus.OK);
    }

    /**
     * Persist a batch of logs.
     * 
     * @param logs a JSON representation of an array of logs.
     */
    @PostMapping("new/batch")
    public ResponseEntity<Log[]> newBatch(@Valid @RequestBody LogDTO[] logs) {
        return new ResponseEntity<>(logService.newBatch(logs), HttpStatus.OK);
    }

}
