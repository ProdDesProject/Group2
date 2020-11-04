package com.oamkgroup2.heartbeat.service;

import java.util.List;

import com.oamkgroup2.heartbeat.model.Log;
import com.oamkgroup2.heartbeat.repository.LogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

	public List<Log> getAll() {
        return logRepository.findAll();
	}

	public Log newLog(String log) {
        //TODO: validate
        Log newLog = new Log();
        newLog.setContent(log);
        return logRepository.save(newLog);
	}
    
}
