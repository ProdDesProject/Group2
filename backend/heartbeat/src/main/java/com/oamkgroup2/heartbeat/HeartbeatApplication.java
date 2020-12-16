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
		int[] arrHill = new int[] { 64, 68, 73, 72, 71, 74, 73, 68, 69, 70, 71, 70, 69, 72, 73, 74, 76, 83, 80, 76, 64,
				62, 61, 60, 58, 59, 62, 63, 60, 67, 69, 65, 63, 68, 69, 66, 64, 63, 65, 66, 65, 67, 66, 69, 67, 66, 67,
				64, 63, 62, 61, 64, 66, 64, 63, 61 };
		int[] arrHammock = new int[] { 77, 81, 74, 67, 76, 65, 73, 59, 60, 64, 65, 65, 68, 65, 66, 66, 70, 72, 66, 74,
				78, 100, 99, 73, 76, 89, 80, 77, 74, 83, 90, 76, 74, 83, 90, 76, 74, 83, 80, 72, 65, 65, 72, 88, 74, 69,
				75, 80, 72, 88, 87, 69, 77, 78, 69, 73 };
		int[] arrDownwardSlope = new int[] { 76, 74, 73, 74, 75, 73, 72, 74, 75, 72, 74, 72, 73, 83, 81, 80, 82, 83, 79,
				72, 73, 74, 77, 79, 76, 74, 73, 75, 73, 70, 68, 69, 70, 72, 70, 69, 65, 67, 70, 73, 72, 71, 72, 73, 74,
				74, 73, 72, 70, 69, 68, 67, 69, 65, 67, 66, 63 };

		exagerateGraph(arrHill);
		exagerateGraph(arrHammock);
		exagerateGraph(arrDownwardSlope);
		exagerateGraph(arrDownwardSlope);

		return args -> {
			for (int i = 0; i < arrHill.length; i++) {
				Log log = new Log();
				log.setUserId(1L);
				LocalDateTime time = LocalDateTime.of(2020, Month.NOVEMBER, 25, 2, 0, 0);
				time = time.plusMinutes((long) i * 5);
				log.setDate(time);
				log.setHeartRate(arrHill[i]);
				log.setSleepSession(1);
				repository.save(log);
			}

			for (int i = 0; i < arrHammock.length; i++) {
				Log log = new Log();
				log.setUserId(1L);
				LocalDateTime time = LocalDateTime.of(2020, Month.NOVEMBER, 26, 2, 0, 0);
				time = time.plusMinutes((long) i * 8);
				log.setDate(time);
				log.setHeartRate(arrHammock[i]);
				log.setSleepSession(2);
				repository.save(log);
			}

			for (int i = 0; i < arrDownwardSlope.length; i++) {
				Log log = new Log();
				log.setUserId(1L);
				LocalDateTime time = LocalDateTime.of(2020, Month.NOVEMBER, 27, 2, 0, 0);
				time = time.plusMinutes((long) i * 6);
				log.setDate(time);
				log.setHeartRate(arrDownwardSlope[i]);
				log.setSleepSession(3);
				repository.save(log);
			}

		};
	}

	private int[] exagerateGraph(int[] input) {
		int avg = getAvg(input);

		for (int i = 0; i < input.length; i++) {
			int deviance = input[i] - avg;
			int newI = input[i] + deviance;
			input[i] = newI;
		}

		return input;
	}

	private int getAvg(int[] input) {
		int total = 0;

		for (int i = 0; i < input.length; i++) {
			total = total + input[i];
		}

		double avg = ((double) total) / input.length;
		return (int) Math.round(avg);
	}

}
