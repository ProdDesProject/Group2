package com.Group2.Heartbeat;

import java.time.LocalDate;

public class NightResult {

    private long userId;
    private String shape;
    private Log[] logs;
    private LocalDate nightStartDate;

    public NightResult(){

    }

    public NightResult(long userId, String shape, Log[] logs, LocalDate nightStartDate) {

        this.setUserId(userId);
        this.setShape(shape);
        this.setLogs(logs);
        this.setNightStartDate(nightStartDate);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Log[] getLogs() {
        return logs;
    }

    public void setLogs(Log[] logs) {
        this.logs = logs;
    }

    public LocalDate getNightStartDate() {
        return nightStartDate;
    }

    public void setNightStartDate(LocalDate nightStartDate) {
        this.nightStartDate = nightStartDate;
    }
}

