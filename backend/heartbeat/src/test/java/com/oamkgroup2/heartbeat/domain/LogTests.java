// package com.oamkgroup2.heartbeat.domain;

// import java.time.LocalDateTime;
// import java.util.logging.Logger;

// import com.oamkgroup2.heartbeat.model.Log;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest
// public class LogTests {

// private static final Logger LOG = Logger.getLogger(LogTests.class.getName());

// Log validLog0 = new Log(0L, LocalDateTime.now(), 0, 0l, 0l);
// Log validLog1 = new Log(1L, LocalDateTime.now(), 1, 1l, 1l);

// Log emptyLog = new Log();
// Log nullLog;

// // #region
// @Test
// public void equalsShouldReturnTrueOnValid() {
// Log validLogCopy = new Log(validLog0.getId(), validLog0.getDate(),
// validLog0.getHeartRate(),
// validLog0.getUserId(), validLog0.getSleepSession());
// assert (validLog0.equals(validLogCopy));
// }

// @Test
// public void equalsShouldReturnFalseOnInValid() {
// Log invalidLogCopy = new Log(validLog0.getId(), validLog0.getDate(),
// validLog0.getHeartRate(),
// validLog0.getUserId(), validLog0.getSleepSession());
// assert (!(validLog1.equals(invalidLogCopy)));
// }

// @Test
// public void equalsShouldReturnFalseOnEmpty() {
// Log validLogCopy = new Log();
// assert (!(validLog0.equals(validLogCopy)));
// }

// @Test
// public void equalsShouldReturnFalseOnEmptyID() {
// Log invalidLogCopy = new Log();
// invalidLogCopy.setDate(validLog0.getDate());
// invalidLogCopy.setHeartRate(validLog0.getHeartRate());
// invalidLogCopy.setUserId(validLog0.getUserId());
// invalidLogCopy.setSleepSession(validLog0.getSleepSession());
// assert (!(validLog0.equals(invalidLogCopy)));
// }

// @Test
// public void equalsShouldReturnFalseOnEmptyDate() {
// Log invalidLogCopy = new Log();
// invalidLogCopy.setId(validLog0.getId());
// invalidLogCopy.setHeartRate(validLog0.getHeartRate());
// invalidLogCopy.setUserId(validLog0.getUserId());
// invalidLogCopy.setSleepSession(validLog0.getSleepSession());
// assert (!(validLog0.equals(invalidLogCopy)));
// }

// @Test
// public void equalsShouldReturnFalseOnEmptyHeartRate() {
// Log invalidLogCopy = new Log();
// invalidLogCopy.setId(validLog0.getId());
// invalidLogCopy.setDate(validLog0.getDate());
// invalidLogCopy.setUserId(validLog0.getUserId());
// invalidLogCopy.setSleepSession(validLog0.getSleepSession());
// assert (!(validLog0.equals(invalidLogCopy)));
// }

// @Test
// public void equalsShouldReturnFalseOnEmptyUserId() {
// Log invalidLogCopy = new Log();
// invalidLogCopy.setId(validLog0.getId());
// invalidLogCopy.setDate(validLog0.getDate());
// invalidLogCopy.setHeartRate(validLog0.getHeartRate());
// invalidLogCopy.setSleepSession(validLog0.getSleepSession());
// assert (!(validLog0.equals(invalidLogCopy)));
// }

// @Test
// public void equalsShouldReturnFalseOnEmptySleepSession() {
// Log invalidLogCopy = new Log();
// invalidLogCopy.setId(validLog0.getId());
// invalidLogCopy.setDate(validLog0.getDate());
// invalidLogCopy.setHeartRate(validLog0.getHeartRate());
// invalidLogCopy.setUserId(validLog0.getUserId());
// assert (!(validLog0.equals(invalidLogCopy)));
// }

// @Test
// public void equalsShouldReturnFalseOnNull() {
// assert (!(validLog0.equals(nullLog)));
// }
// // #endregion

// @Test
// public void equalsWithoutIdShouldReturnTrueOnValid() {
// Log validLogCopy = new Log(validLog0.getId(), validLog0.getDate(),
// validLog0.getHeartRate(),
// validLog0.getUserId(), validLog0.getSleepSession());
// assert (validLog0.equalsWithoutId(validLogCopy));
// }

// @Test
// public void equalsWithoutIdShouldReturnFalseOnInValid() {
// Log invalidLogCopy = new Log(validLog0.getId(), validLog0.getDate(),
// validLog0.getHeartRate(),
// validLog0.getUserId(), validLog0.getSleepSession());
// assert (!(validLog1.equalsWithoutId(invalidLogCopy)));
// }

// @Test
// public void equalsWithoutIdShouldReturnFalseOnEmpty() {
// Log validLogCopy = new Log();
// assert (!(validLog0.equalsWithoutId(validLogCopy)));
// }

// @Test
// public void equalsWithoutIdShouldReturnTrueOnEmptyID() {
// Log invalidLogCopy = new Log();
// invalidLogCopy.setDate(validLog0.getDate());
// invalidLogCopy.setHeartRate(validLog0.getHeartRate());
// invalidLogCopy.setUserId(validLog0.getUserId());
// invalidLogCopy.setSleepSession(validLog0.getSleepSession());
// assert (validLog0.equalsWithoutId(invalidLogCopy));
// }

// @Test
// public void equalsWithoutIdShouldReturnFalseOnEmptyDate() {
// Log invalidLogCopy = new Log();
// invalidLogCopy.setHeartRate(validLog0.getHeartRate());
// invalidLogCopy.setUserId(validLog0.getUserId());
// invalidLogCopy.setSleepSession(validLog0.getSleepSession());
// assert (!(validLog0.equalsWithoutId(invalidLogCopy)));
// }

// @Test
// public void equalsWithoutIdShouldReturnFalseOnEmptyHeartRate() {
// Log invalidLogCopy = new Log();
// invalidLogCopy.setDate(validLog0.getDate());
// invalidLogCopy.setUserId(validLog0.getUserId());
// invalidLogCopy.setSleepSession(validLog0.getSleepSession());
// assert (!(validLog0.equalsWithoutId(invalidLogCopy)));
// }

// @Test
// public void equalsWithoutIdShouldReturnFalseOnEmptyUserId() {
// Log invalidLogCopy = new Log();
// invalidLogCopy.setDate(validLog0.getDate());
// invalidLogCopy.setHeartRate(validLog0.getHeartRate());
// invalidLogCopy.setSleepSession(validLog0.getSleepSession());
// assert (!(validLog0.equalsWithoutId(invalidLogCopy)));
// }

// @Test
// public void equalsWithoutIdShouldReturnFalseOnEmptySleepSession() {
// Log invalidLogCopy = new Log();
// LOG.info("sleepsession before setting: " + invalidLogCopy.getSleepSession());
// LOG.info("userid before setting: " + invalidLogCopy.getUserId());
// invalidLogCopy.setDate(validLog0.getDate());
// invalidLogCopy.setHeartRate(validLog0.getHeartRate());
// invalidLogCopy.setUserId(validLog0.getUserId());
// LOG.info(validLog0.toString() + '\n' + invalidLogCopy.toString());
// assert (!(validLog0.equalsWithoutId(invalidLogCopy)));
// }

// @Test
// public void equalsWithoutIdShouldReturnFalseOnNull() {
// assert (!(validLog0.equalsWithoutId(nullLog)));
// }

// }
