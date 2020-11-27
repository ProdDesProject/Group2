package com.oamkgroup2.heartbeat.controllerIT;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.oamkgroup2.heartbeat.model.Log;
import com.oamkgroup2.heartbeat.repository.LogRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class LogControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LogRepository logRepository;

    private Gson GSON = new Gson();
    private static final Logger LOG = Logger.getLogger(LogControllerIT.class.getName());

    Log log1 = new Log(0L, LocalDateTime.now(), 0, 0L, 0);

    // TODO: add before test

    @Test
    public void getAllShouldBeEmptyBeforeAdding() throws Exception {
        LOG.info("starting getAllShouldBeEmptyBeforeAdding...");
        // TODO: add persistent database for running and in-memory for testing
        // TODO: add http responses
        this.mockMvc.perform(get("/logs/getall")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    public void getAllShouldHaveContentAfterAdding() throws Exception {
        LOG.info("starting getAllShouldHaveContentAfterAdding...");
        // TODO: add persistent database for running and in-memory for testing
        logRepository.save(log1);
        this.mockMvc.perform(get("/logs/getall")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(GSON.toJson(log1), true));
    }

    @Test
    public void newLogShouldSaveAndReturnValidLog() throws Exception {
        assert (false);
    }

    @Test
    public void newLogShouldThrowErrorCodeInvalidLog() throws Exception {
        assert (false);
    }

    @Test
    public void newBatchShouldSaveAndReturnValidLog() throws Exception {
        assert (false);
    }

    @Test
    public void newBatchShouldThrowErrorCodeInvalidLog() throws Exception {
        assert (false);
    }

}
