package com.oamkgroup2.heartbeat.controller;
import java.util.List;


import com.oamkgroup2.heartbeat.service.LogService;
import com.oamkgroup2.heartbeat.model.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/logtimestamped")
public class LogTimestamp {

    //     Timestamp timestamp = new Timestamp(System.currentTimeMillis)
    //     Date date = new Date();
    //     System.out.println(new Timestamp(date.getTime));
    // }


    @Autowired
    private LogService logService;


    @GetMapping("/getall")
    List<Log> getAll() {
        return logService.getAll();
    }
 
    @PostMapping("new/log")
    Log newLog(@RequestBody String log, String timestamp){
        return logService.newLog(log);
    }

}
    

