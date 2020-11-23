package com.oamkgroup2.heartbeat.model;

public class NightResult {

    Long userId;
    NightResult shape;
    Log[] logs;

    public NightResult(Long userId, NightResult shape, Log[] logs) {
        this.userId = userId;
        this.shape = shape;
        this.logs = logs;
    }

    public NightResult() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public NightResult getShape() {
        return shape;
    }

    public void setShape(NightResult shape) {
        this.shape = shape;
    }

    public Log[] getLogs() {
        return logs;
    }

    public void setLogs(Log[] logs) {
        this.logs = logs;
    }

}
