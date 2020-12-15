package com.oamkgroup2.heartbeat.controller;

import com.oamkgroup2.heartbeat.exception.EntityNotFoundException;
import com.oamkgroup2.heartbeat.model.NightResult;
import com.oamkgroup2.heartbeat.service.ResultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API controller for handling requests about NightResults.
 */
@RestController
@RequestMapping("/results")
public class ResultController {

    @Autowired
    ResultService resultService;

    /**
     * Get the latest results for a specific user.
     * 
     * @throws EntityNotFoundException
     */
    @GetMapping("/get/latest")
    public ResponseEntity<NightResult> getLatestResultByUserId(@RequestBody long userId)
            throws EntityNotFoundException {
        return new ResponseEntity<>(this.resultService.getLatestResult(userId), HttpStatus.OK);
    }

    @GetMapping("/get/specific")
    public ResponseEntity<NightResult> getSpecificNightResult(@RequestParam(name = "userId") long userId,
            @RequestParam(name = "sleepsession") long sleepsession) throws EntityNotFoundException {
        return new ResponseEntity<>(this.resultService.getSpecificResult(userId, sleepsession), HttpStatus.OK);
    }

    /**
     * Test method that does the same as getUserById but returns a default set of
     * testdata.
     * 
     * @throws EntityNotFoundException
     */
    @GetMapping("/get/test")
    public ResponseEntity<NightResult> getTestResult() throws EntityNotFoundException {
        return new ResponseEntity<>(this.resultService.getTestResult(), HttpStatus.OK);
    }

}
