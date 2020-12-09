package com.Group2.Heartbeat;

public class Log {

    private long id;
    private String date;
    private int heartRate;
    private long userID;
    private long sleepSession;

    public Log() {

    }

    public Log(long id, String date, int heartRate, long userID, long sleepSession) {
        this.id = id;
        this.date = date;
        this.heartRate = heartRate;
        this.userID = userID;
        this.sleepSession = sleepSession;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getSleepSession() {
        return sleepSession;
    }

    public void setSleepSession(long sleepSession) {
        this.sleepSession = sleepSession;
    }
}