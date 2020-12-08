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
        if (userId >= 0) {
            NightResult result = new NightResult();
            result.setUserId(userId);
            result.setShape(ShapeResult.UNDEFINED);
            Log[] lastNight = this.logRepository.findLastNightLogsForUser(userId);
            result.setLogs(lastNight);
            // TODO: analyze ShapeResult
            if (lastNight.length > 0) {
                return result;
            }
            throw new EntityNotFoundException("logs for user with id: " + userId);
        }
        throw new EntityNotFoundException(" user with id: " + userId);
    }

    /**
     * Return a set of testdata.
     * 
     * @throws EntityNotFoundException
     */
    public NightResult getTestResult() throws EntityNotFoundException {
        return this.getLatestResult(0);
    }

    /**
     * Return the NightResult for a specific user on a specific night.
     * 
     * @throws EntityNotFoundException
     */
    public NightResult getSpecificResult(long userId, long sleepSession) throws EntityNotFoundException {
        if (sleepSession >= 0 && userId >= 0) {
            NightResult result = new NightResult();
            result.setUserId(userId);
            result.setShape(ShapeResult.UNDEFINED);
            Log[] lastNight = this.logRepository.findNightsByUserAndSleepSession(userId, sleepSession);
            result.setLogs(lastNight);
            // TODO: analyze ShapeResult
            if (lastNight.length > 0) {
                return result;
            }
            throw new EntityNotFoundException(
                    "logs for user with id: " + userId + " and sleepsession with id: " + sleepSession);
        }
        throw new EntityNotFoundException(" user with id: " + userId + " and sleepsession with id: " + sleepSession);
    }

}
