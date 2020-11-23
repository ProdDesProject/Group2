package com.oamkgroup2.heartbeat.repository;

import com.oamkgroup2.heartbeat.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
