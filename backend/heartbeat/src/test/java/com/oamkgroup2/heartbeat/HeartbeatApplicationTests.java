package com.oamkgroup2.heartbeat;

import com.oamkgroup2.heartbeat.controller.LogController;
import com.oamkgroup2.heartbeat.controller.ResultController;
import com.oamkgroup2.heartbeat.controller.UserController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.logging.Logger;

@SpringBootTest
class HeartbeatApplicationTests {

	private static final Logger LOG = Logger.getLogger(HeartbeatApplicationTests.class.getName());

	@Autowired
	LogController logController;

	@Autowired
	ResultController resultController;

	@Autowired
	UserController userController;

	@Test
	void contextLoads() {
		LOG.info("Starting contextLoads...");
		assertThat(logController).isNotNull();
		assertThat(resultController).isNotNull();
		assertThat(userController).isNotNull();
	}

}
