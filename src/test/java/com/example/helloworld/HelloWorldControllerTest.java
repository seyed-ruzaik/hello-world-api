package com.example.helloworld;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration-style tests for the HelloWorldController.
 *
 * These tests:
 * - Start a minimal Spring context.
 * - Use MockMvc to simulate HTTP requests.
 * - Verify HTTP status codes and JSON bodies.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class HelloWorldControllerTest {

    /**
     * MockMvc is a helper object that allows us to perform
     * HTTP calls against our controller without starting a real server.
     */
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return 200 and greeting when name starts with A–M (e.g., alice)")
    void shouldReturnGreetingForFirstHalfName() throws Exception {
        mockMvc.perform(get("/hello-world")
                        .param("name", "alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello Alice"));
    }

    @Test
    @DisplayName("Should return 200 and greeting when name is lower-case but starts with B (e.g., bob)")
    void shouldReturnGreetingForLowercaseName() throws Exception {
        mockMvc.perform(get("/hello-world")
                        .param("name", "bob"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello Bob"));
    }

    @Test
    @DisplayName("Should return 400 and error when first letter is in N–Z (e.g., zed)")
    void shouldReturnBadRequestForSecondHalfName() throws Exception {
        mockMvc.perform(get("/hello-world")
                        .param("name", "zed"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Input"));
    }

    @Test
    @DisplayName("Should return 400 and error when name parameter is missing")
    void shouldReturnBadRequestWhenNameMissing() throws Exception {
        mockMvc.perform(get("/hello-world"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Input"));
    }

    @Test
    @DisplayName("Should return 400 and error when name is empty string")
    void shouldReturnBadRequestWhenNameEmpty() throws Exception {
        mockMvc.perform(get("/hello-world")
                        .param("name", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Input"));
    }

    @Test
    @DisplayName("Should return 400 and error when name is only spaces")
    void shouldReturnBadRequestWhenNameOnlySpaces() throws Exception {
        mockMvc.perform(get("/hello-world")
                        .param("name", "   "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Input"));
    }

    @Test
    @DisplayName("Should return 400 and error when name starts with non-letter (e.g., 1alice)")
    void shouldReturnBadRequestWhenNameStartsWithNonLetter() throws Exception {
        mockMvc.perform(get("/hello-world")
                        .param("name", "1alice"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Input"));
    }
}
