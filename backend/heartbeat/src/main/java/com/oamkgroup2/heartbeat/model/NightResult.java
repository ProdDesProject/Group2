package com.oamkgroup2.heartbeat.model;

import java.time.LocalDate;
import java.time.ZoneId;

public class NightResult {

    Long userId;
    ShapeResult shape;
    Log[] logs;
    LocalDate nightStartDate;

    public NightResult(Long userId, ShapeResult shape, Log[] logs) {
        this.userId = userId;
        this.shape = shape;
        this.logs = logs;
        if (this.logs.length > 0) {
            this.nightStartDate = logs[0].getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

    public NightResult() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ShapeResult getShape() {
        return shape;
    }

    public void setShape(ShapeResult shape) {
        this.shape = shape;
    }

    public Log[] getLogs() {
        return logs;
    }

    public void setLogs(Log[] logs) {
        this.logs = logs;
    }

}
