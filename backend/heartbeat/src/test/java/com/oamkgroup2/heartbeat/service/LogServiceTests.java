package com.oamkgroup2.heartbeat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.oamkgroup2.heartbeat.model.Log;
import com.oamkgroup2.heartbeat.repository.LogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

@SpringBootTest
public class LogServiceTests {

    @Autowired
    LogService service;

    @Autowired
    LogRepository repo;

    Gson GSON = new Gson();

    Log validLog0 = new Log(0L, LocalDateTime.now(), 0, 0l, 0l);
    Log validLog1 = new Log(1L, LocalDateTime.now(), 1, 1l, 1l);

    Log emptyLog = new Log();
    Log nullLog;

    // getall
    // TODO: test this when testing db is set up.

    // newlog
    @Test
    public void newLogShouldReturnNewLog() {
        Log returnLog = service.newLog(validLog0);
        String stringReturnLog = GSON.toJson(returnLog);
        String stringValidLog0 = GSON.toJson(validLog0);
        assertEquals(stringReturnLog, stringValidLog0);
    }

    @Test
    public void newLogShouldIncreaseDbSize() {

    }

    @Test
    public void newLogShouldPersistLog() {

    }

    public void emptyLogShouldBeRejected() {

    }

    // newbatch

}
