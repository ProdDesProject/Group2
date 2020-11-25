package com.oamkgroup2.heartbeat.model;

import java.time.LocalDate;
import java.time.Month;

/**
 * Class that represents a specific night for a user. Contains HeartrateLogs,
 * the ShapeResult and the night the sleep belongs to.
 */
public class NightResult {

    /**
     * The user this NightResult belongs to.
     */
    Long userId;

    /**
     * The calculated shape the heartratelogs in this night represent. See
     * ShapeResult docs for more info.
     */
    ShapeResult shape;

    /**
     * The Heartrate logs for this night.
     */
    Log[] logs;

    /**
     * The date this night started.
     */
    LocalDate nightStartDate;

    public NightResult(Long userId, ShapeResult shape, Log[] logs) {
        this.userId = userId;
        this.shape = shape;
        this.logs = logs;
        if (this.logs.length > 0) {
            int year = logs[0].getDate().getYear();
            Month month = logs[0].getDate().getMonth();
            int day = logs[0].getDate().getDayOfMonth();
            LocalDate date = LocalDate.of(year, month, day);
            this.nightStartDate = date;
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
