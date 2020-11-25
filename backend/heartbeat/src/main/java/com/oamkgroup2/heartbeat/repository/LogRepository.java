package com.oamkgroup2.heartbeat.repository;

import java.util.Date;

import com.oamkgroup2.heartbeat.model.Log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LogRepository extends JpaRepository<Log, Long> {

    // SELECT all logs from the latest sleepsession for a specific user
    @Query("SELECT log FROM Heartbeat_logs" + "WHERE (userId = user AND sleepSession" + "IN (SELECT max(sleepSession)"
            + "FROM Heartbeat_logs WHERE userId = user)")
    Log[] findLastNightLogsForUser(long user);

}
