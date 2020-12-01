package com.oamkgroup2.heartbeat.service;

import java.util.logging.Logger;

import com.oamkgroup2.heartbeat.model.NightResult;
import com.oamkgroup2.heartbeat.model.ShapeResult;
import com.oamkgroup2.heartbeat.repository.LogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oamkgroup2.heartbeat.exception.EntityNotFoundException;
import com.oamkgroup2.heartbeat.model.Log;

@Service
public class ResultService {

    @Autowired
    private LogRepository logRepository;

    private static final Logger LOG = Logger.getLogger(ResultService.class.getName());

    /**
     * Get the NightResult for the last night of a specific user.
     * 
     * @param userId the user to get the result for.
     * @return NightResult representing the heartrate logs and analyzed graphed
     *         shape of those logs.
     * @throws EntityNotFoundException
     */
    public NightResult getLatestResult(long userId) throws EntityNotFoundException {
        NightResult result = new NightResult();
        result.setUserId(userId);
        result.setShape(ShapeResult.UNDEFINED);
        Log[] lastNight = this.logRepository.findLastNightLogsForUser(userId);
        result.setLogs(lastNight);
        // TODO: analyze ShapeResult
        if (lastNight.length > 0) {
            return result;
        }
        throw new EntityNotFoundException("user with id: " + userId);
    }

    /**
     * Return a set of testdata.
     * 
     * @throws EntityNotFoundException
     */
    public NightResult getTestResult() throws EntityNotFoundException {
        return this.getLatestResult(0);
    }

}
