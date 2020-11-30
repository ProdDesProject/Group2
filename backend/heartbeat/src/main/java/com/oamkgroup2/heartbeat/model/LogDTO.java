package com.oamkgroup2.heartbeat.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Class that represents a heartrate/min for a specific time for a specific
 * user.
 */
public class LogDTO {

    /**
     * The date and time for this specific log, in miliseconds since epoch.
     */
    @Min(0)
    @NotNull
    private long epochDate;

    /**
     * The heartRate per minute for this specific time.
     */
    @Min(0)
    @NotNull
    private int heartRate;

    /**
     * The user this log belongs to.
     */
    @Min(0)
    @NotNull
    private long userId;

    /**
     * The night this log belongs to. Every sleepsession represents one night.
     */
    @Min(0)
    @NotNull
    private long sleepSession;

    public LogDTO() {
    }

    public LogDTO(long epochDate, int heartRate, long userId, long sleepSession) {
        this.epochDate = epochDate;
        this.heartRate = heartRate;
        this.userId = userId;
        this.sleepSession = sleepSession;
    }

    public long getEpochDate() {
        return this.epochDate;
    }

    public void setDate(long epochDate) {
        this.epochDate = epochDate;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSleepSession() {
        return sleepSession;
    }

    public void setSleepSession(long sleepSession) {
        this.sleepSession = sleepSession;
    }

    /**
     * Method for comparing Logs. Does include Log ID in comparison.
     */
    @Override
    public boolean equals(Object logObject) {
        try {
            if (logObject.getClass() != LogDTO.class) {
                return false;
            } else {
                LogDTO log = (LogDTO) logObject;

                if (this.epochDate != log.getEpochDate()) {
                    return false;
                }

                if (this.heartRate != log.getHeartRate()) {
                    return false;
                }

                if (this.userId != log.getUserId()) {
                    return false;
                }

                if (this.sleepSession != log.getSleepSession()) {
                    return false;
                }

                return true;
            }

        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Method for comparing Log objects. Discounts the Log ID, since this only gets
     * set when persisted.
     */
    public boolean equalsWithoutId(Object objectLog) {
        try {
            if (objectLog.getClass() != Log.class) {
                return false;
            } else {
                LogDTO log = (LogDTO) objectLog;
                if (this.epochDate != log.getEpochDate()) {
                    return false;
                }

                if (this.heartRate != log.getHeartRate()) {
                    return false;
                }

                if (this.userId != log.getUserId()) {
                    return false;
                }

                if (this.sleepSession != log.getSleepSession()) {
                    return false;
                }

                return true;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("EpochDate: %d, heartrate: %d, userid: %d, sleepsession: %d.", this.epochDate,
                this.heartRate, this.userId, this.sleepSession);
    }

}
