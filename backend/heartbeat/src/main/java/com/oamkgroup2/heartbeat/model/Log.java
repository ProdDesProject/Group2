package com.oamkgroup2.heartbeat.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Heartbeat_logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Date date;
    int heartRate;

    public Log() {
    }

    Log(long id, Date date, int heartRate) {
        this.id = id;
        this.date = date;
        this.heartRate = heartRate;
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

}
