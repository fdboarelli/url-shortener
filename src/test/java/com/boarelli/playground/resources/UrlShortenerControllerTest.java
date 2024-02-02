package com.boarelli.playground.resources;

import com.boarelli.playground.helper.IntegrationTest;
import com.boarelli.playground.model.dtos.UrlDTO;
import com.boarelli.playground.model.entities.UrlEntity;
import com.boarelli.playground.repository.UrlEntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

@AutoConfigureMockMvc
public class UrlShortenerControllerTest extends IntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UrlEntityRepository urlEntityRepository;
    private final String testUrl = "https://my-test-url-to-be-compressed.com/because-it-is-too-long";
    private final String compressedUrlPattern = "https://test-url\\.com/[a-zA-Z0-9]{9,}$";

    private final UrlEntity existingUrl;

    @Autowired
    public UrlShortenerControllerTest(MockMvc mockMvc, UrlEntityRepository urlEntityRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.urlEntityRepository = urlEntityRepository;
        existingUrl = UrlEntity.builder().originalUrl("https://very-long-existing-url.com")
                .shortUrl("https://test-url.com/shorturl").build();
    }

    @BeforeEach
    void init() {
        urlEntityRepository.save(existingUrl);
    }

    @AfterEach
    void cleanUp() {
        urlEntityRepository.deleteAll();
    }

    @DisplayName("create url - ok")
    @Test
    void createShortUrlReturnOk() throws Exception {
        var response = this.mockMvc.perform(post("/v1/urls").content("{\"url\":\""+testUrl+"\"}")
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        var compressedUrl = response.substring(response.indexOf("https://test-url.com/"), response.indexOf("\"}"));
        assertTrue(compressedUrl.matches(compressedUrlPattern));
    }

    @DisplayName("get url - ok")
    @Test
    void getUrlOk() throws Exception {
        var response = this.mockMvc.perform(get("/v1/urls/"+existingUrl.getId()))
                .andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @DisplayName("get url - bad request")
    @Test
    void getUrlBadRequest() throws Exception {
        this.mockMvc.perform(get("/v1/urls/ ")).andDo(print()).andExpect(status().isBadRequest());
    }

    @DisplayName("create url and return all urls - ok")
    @Test
    void createShortUrlAndReturnAllUrlsOk() throws Exception {
        var response = this.mockMvc.perform(post("/v1/urls").content("{\"url\":\""+testUrl+"\"}")
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        var compressedUrl = response.substring(response.indexOf("https://test-url.com/"), response.indexOf("\"}"));
        assertTrue(compressedUrl.matches(compressedUrlPattern));
        var allUrlsResponse = this.mockMvc.perform(get("/v1/urls").contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        var urlDtoList = Arrays.asList(objectMapper.readValue(allUrlsResponse, UrlDTO[].class));
        assertEquals(2, urlDtoList.size());
    }

    @DisplayName("create empty url - bad request")
    @Test
    void createEmptyUrlBadRequest() throws Exception {
        this.mockMvc.perform(post("/v1/urls").content("{\"url\":\" \"}")
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());
    }

    @DisplayName("create not valid url - bad request")
    @Test
    void createNotValidUrlBadRequest() throws Exception {
        this.mockMvc.perform(post("/v1/urls").content("{\"url\":\"this is not a valid url!\"}")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());
    }


    @DisplayName("delete url - ok")
    @Test
    void deleteUrlOk() throws Exception {
        this.mockMvc.perform(delete("/v1/urls/"+existingUrl.getId()))
                .andDo(print()).andExpect(status().isNoContent());
    }

    @DisplayName("delete url - bad request")
    @Test
    void deleteUrlBadRequest() throws Exception {
        this.mockMvc.perform(delete("/v1/urls/ ")).andDo(print()).andExpect(status().isBadRequest());
    }

}
