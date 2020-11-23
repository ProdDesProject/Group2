package com.oamkgroup2.heartbeat.service;

import java.util.List;

import com.google.gson.Gson;
import com.oamkgroup2.heartbeat.model.Log;
import com.oamkgroup2.heartbeat.repository.LogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    private final Gson GSON = new Gson();
    private static final Logger LOG = Logger.getLogger(LogService.class.getName());

    public List<Log> getAll() {
        return logRepository.findAll();
    }

    public Log newLog(String stringLog) {
        try {
            Log newLog = GSON.fromJson(stringLog, Log.class);
            return logRepository.save(newLog);
        } catch (Exception e) {
            LOG.warning(e.getLocalizedMessage());
            return null;
        }
    }

}
