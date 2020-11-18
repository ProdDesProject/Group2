package com.oamkgroup2.heartbeat.repository;

import com.oamkgroup2.heartbeat.model.LogTimestamped;

import org.springframework.data.jpa.repository.JpaRepository;


public interface LogTimestampedRepository extends JpaRepository<LogTimestamped, Long> {

}
