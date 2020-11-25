package com.oamkgroup2.heartbeat.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "Heartbeat_logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Temporal(TemporalType.TIMESTAMP)
    Date date;
    int heartRate;
    long userId;
    long sleepSession;

    public Log() {
    }

    Log(long id, Date date, int heartRate, long userId, long sleepSession) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
