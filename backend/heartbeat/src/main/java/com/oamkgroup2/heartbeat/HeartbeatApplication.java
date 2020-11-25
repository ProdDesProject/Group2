package com.oamkgroup2.heartbeat;

import java.time.LocalDateTime;
import java.time.Month;

import com.oamkgroup2.heartbeat.model.Log;
import com.oamkgroup2.heartbeat.model.User;
import com.oamkgroup2.heartbeat.repository.LogRepository;
import com.oamkgroup2.heartbeat.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HeartbeatApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeartbeatApplication.class, args);
	}

	@Bean
	public CommandLineRunner persistUser(UserRepository repository) {
		return args -> {
			User user = new User();
			user.setAge(25);
			user.setId(0L);
			user.setName("testuser");

			repository.save(user);
		};
	}

	@Bean
	public CommandLineRunner persistLogs(LogRepository repository) {
		return args -> {
			Log log = new Log();
			log.setSleepSession(0);
			log.setUserId(0L);
			LocalDateTime time = LocalDateTime.of(2020, Month.NOVEMBER, 25, 2, 0, 0);
			for (int i = 0; i < 60; i++) {
				time = time.plusMinutes((long) i);
				log.setDate(time);
				log.setHeartRate(i);
				log.setId((long) i);

				repository.save(log);
			}
		};
	}

}
