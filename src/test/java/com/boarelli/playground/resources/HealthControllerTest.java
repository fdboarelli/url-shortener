package com.boarelli.playground.resources;

import com.boarelli.playground.helper.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class HealthControllerTest extends IntegrationTest {

    private final MockMvc mockMvc;

    @Autowired
    public HealthControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @DisplayName("get health exception ok")
    @Test
    void getHealthShouldReturnOk() throws Exception {
        this.mockMvc.perform(get("/v1/health")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"message\":\"service is healthy\"}")));
    }

}
