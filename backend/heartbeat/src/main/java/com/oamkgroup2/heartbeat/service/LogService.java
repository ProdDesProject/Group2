package com.oamkgroup2.heartbeat.service;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.oamkgroup2.heartbeat.model.Log;
import com.oamkgroup2.heartbeat.model.LogDTO;
import com.oamkgroup2.heartbeat.repository.LogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.validation.Valid;

/**
 * Service that provides tools for saving and getting logs.
 */
@Service
@Validated
public class LogService {

    @Autowired
    private LogRepository logRepository;

    /**
     * Tool for converting to and from JSON and POJO objects.
     */
    private final Gson GSON = new Gson();
    private static final Logger LOG = Logger.getLogger(LogService.class.getName());

    /**
     * Gets all logs in the database. Only for testing. TODO: remove this for
     * production.
     */
    public List<Log> getAll() {
        return logRepository.findAll();
    }

    /**
     * Create a new log from a JSON representation of a log and persist it.
     * 
     * @param stringLog JSON representation of a log.
     */
    public Log newLog(@Valid LogDTO logDTO) {
        Log log = new Log(logDTO);
        return logRepository.save(log);
    }

    /**
     * Saves a batch of logs from a JSON representation of an array of logs and
     * persist them as seperate logs.
     * 
     * @param stringLog JSON representation of an array of logs.
     */
    public Log[] newBatch(@Valid LogDTO[] logDTOs) {
        try {
            List<Log> logs = Arrays.stream(logDTOs).map(dto -> new Log(dto)).collect(Collectors.toList());
            return logRepository.saveAll(logs).toArray(new Log[0]);
        } catch (Exception e) {
            LOG.warning(e.getLocalizedMessage());
            // TODO: fix error handling
            return new Log[0];
        }
    }

}
