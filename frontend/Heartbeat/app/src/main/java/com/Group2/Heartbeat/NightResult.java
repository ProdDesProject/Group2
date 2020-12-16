package com.Group2.Heartbeat;

import java.time.LocalDate;

public class NightResult {

    private long userId;
    private String shape;
    private Log[] logs;

    public NightResult(){

    }

    public NightResult(long userId, String shape, Log[] logs) {

        this.setUserId(userId);
        this.setShape(shape);
        this.setLogs(logs);
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

}