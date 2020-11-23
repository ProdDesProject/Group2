package com.oamkgroup2.heartbeat.repository;

import java.util.Date;

import com.oamkgroup2.heartbeat.model.Log;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {

    Log[] findAllByDateAndUserId(Date date, Long userId);

}
