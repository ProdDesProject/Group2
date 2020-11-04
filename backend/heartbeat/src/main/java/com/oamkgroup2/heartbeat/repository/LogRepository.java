package com.oamkgroup2.heartbeat.repository;

import com.oamkgroup2.heartbeat.model.Log;

import org.springframework.data.jpa.repository.JpaRepository;


public interface LogRepository extends JpaRepository<Log, Long> {

}
