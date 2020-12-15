package com.oamkgroup2.heartbeat.service;

import java.util.Arrays;
import java.util.List;

import com.oamkgroup2.heartbeat.exception.EntityNotFoundException;
import com.oamkgroup2.heartbeat.model.Log;
import com.oamkgroup2.heartbeat.model.LogDTO;
import com.oamkgroup2.heartbeat.repository.LogRepository;
import com.oamkgroup2.heartbeat.repository.UserRepository;

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

    @Autowired
    private UserRepository userRepository;

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
        List<Log> logs = Arrays.stream(logDTOs).map(dto -> new Log(dto)).collect(Collectors.toList());
        return logRepository.saveAll(logs).toArray(new Log[0]);
    }

    public long getLatestSleepSession(Long userId) throws EntityNotFoundException {
        if (userRepository.existsById(userId)) {
            long[] sleepSessions = logRepository.findTopSleepSessionsForUser(userId);
            LOG.info("sleepsession length: " + sleepSessions.length);
            if (sleepSessions.length > 0) {
                return sleepSessions[0];
            }
            return 0;
        }
        throw new EntityNotFoundException("user with id: " + userId);
    }

}
