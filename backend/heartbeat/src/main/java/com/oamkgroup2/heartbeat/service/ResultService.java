package com.oamkgroup2.heartbeat.service;

import java.util.logging.Logger;

import com.oamkgroup2.heartbeat.model.NightResult;
import com.oamkgroup2.heartbeat.model.ShapeResult;
import com.oamkgroup2.heartbeat.repository.LogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     */
    public NightResult getLatestResult(long userId) {
        NightResult result = new NightResult();
        result.setUserId(userId);
        result.setShape(ShapeResult.UNDEFINED);
        try {
            Log[] lastNight = this.logRepository.findLastNightLogsForUser(userId);
            result.setLogs(lastNight);
            // TODO: analyze ShapeResult
            return result;
        } catch (Exception e) {
            LOG.warning(e.getLocalizedMessage());
            // TODO: fix error handling
            return result;
        }
    }

    /**
     * Return a set of testdata.
     */
    public NightResult getTestResult() {
        return this.getLatestResult(0);
    }

}
