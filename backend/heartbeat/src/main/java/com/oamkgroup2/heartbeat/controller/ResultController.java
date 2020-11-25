package com.oamkgroup2.heartbeat.controller;

import com.oamkgroup2.heartbeat.model.NightResult;
import com.oamkgroup2.heartbeat.service.ResultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
public class ResultController {

    @Autowired
    ResultService resultService;

    @GetMapping("/get/latest")
    public NightResult getUserById(@RequestBody long userId) {
        return this.resultService.getLatestResult(userId);
    }

}
