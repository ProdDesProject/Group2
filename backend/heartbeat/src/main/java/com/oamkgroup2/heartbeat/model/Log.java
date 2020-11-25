package com.oamkgroup2.heartbeat.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class that represents a heartrate/min for a specific time for a specific
 * user.
 */
@Entity
@Table(name = "Heartbeat_logs")
public class Log {

    /**
     * The log id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    /**
     * The date and time for this specific log.
     */
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime date;

    /**
     * The heartRate per minute for this specific time.
     */
    int heartRate;

    /**
     * The user this log belongs to.
     */
    long userId;

    /**
     * The night this log belongs to. Every sleepsession represents one night.
     */
    long sleepSession;

    public Log() {
    }

    Log(long id, LocalDateTime date, int heartRate, long userId, long sleepSession) {
        this.id = id;
        this.date = date;
        this.heartRate = heartRate;
        this.userId = userId;
        this.sleepSession = sleepSession;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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

}
