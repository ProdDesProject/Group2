package com.oamkgroup2.heartbeat.repository;

import com.oamkgroup2.heartbeat.model.Log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LogRepository extends JpaRepository<Log, Long> {

    // SELECT all logs from the latest sleepsession for a specific user
    @Query(value = "SELECT * FROM Heartbeat_logs l"
            + " WHERE l.user_id = :user AND l.sleep_session IN (SELECT max(sleep_session) FROM Heartbeat_logs h WHERE h.user_id = :user)", nativeQuery = true)
    Log[] findLastNightLogsForUser(@Param("user") long user);

    @Query(value = "SELECT sleep_session FROM Heartbeat_logs l WHERE l.user_id = :user ORDER BY sleep_session DESC LIMIT 3;", nativeQuery = true)
    long[] findTopSleepSessionsForUser(@Param("user") long user);

}
