package com.oamkgroup2.heartbeat.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Class that represents a heartrate/min for a specific time for a specific
 * user.
 */
@Entity
@Table(name = "heartbeatlogs")
public class Log {

    /**
     * The log id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * The date and time for this specific log.
     */
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "logdate")
    LocalDateTime date;

    /**
     * The heartRate per minute for this specific time.
     */
    @Min(0)
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "heartrate")
    int heartRate;

    /**
     * The user this log belongs to.
     */
    @Min(0)
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "userid")
    long userId;

    /**
     * The night this log belongs to. Every sleepsession represents one night.
     */
    @Min(0)
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "sleepsession")
    long sleepSession;

    public Log() {
    }

    public Log(LogDTO dto) {
        this.date = LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.getEpochDate()), ZoneId.systemDefault());
        this.heartRate = dto.getHeartRate();
        this.userId = dto.getUserId();
        this.sleepSession = dto.getSleepSession();
    }

    public Log(long id, LocalDateTime date, int heartRate, long userId, long sleepSession) {
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

    /**
     * Method for comparing Logs. Does include Log ID in comparison.
     */
    @Override
    public boolean equals(Object logObject) {
        try {
            if (logObject.getClass() != Log.class) {
                return false;
            } else {
                Log log = (Log) logObject;

                if (this.id != log.getId()) {
                    return false;
                }

                if (this.date != log.getDate()) {
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
                Log log = (Log) objectLog;
                if (this.date != log.getDate()) {
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
        return String.format("Log: id: %s, date: %s, heartrate: %d, userid: %d, sleepsession: %d.", this.id,
                this.date.toString(), this.heartRate, this.userId, this.sleepSession);
    }

}
