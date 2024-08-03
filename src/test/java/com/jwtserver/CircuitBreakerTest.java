package com.jwtserver;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jwtserver.user.adapter.in.make_token.MakeTokenRequest;
import com.jwtserver.user.adapter.in.reissue_token.ReIssueTokenRequest;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class CircuitBreakerTest extends IntegrationTestSupport {

    private String userAgent;

    private CircuitBreaker circuitBreaker;

    @BeforeEach
    void setup() {
        this.userAgent = "test1";
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("redis");
    }

    @Test
    @DisplayName("[success] 레디스 이슈로 서킷브레이커가 오픈되었을 때 DB 데이터를 사용하여 정상적으로 인증토큰이 재발급되는지 확인한다.")
    void success() throws Exception {

        // ------------------ STEP 1 : 토큰 발급 ------------------
        // given
        MakeTokenRequest request = MakeTokenRequest.builder()
            .username("user")
            .password("1234")
            .build();

        // when
        ResultActions actions = mockMvc.perform(
            post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", userAgent)
                .content(objectMapper.writeValueAsString(request)));

        // then
        actions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.httpStatus").value(200))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data.accessToken").exists())
            .andExpect(jsonPath("$.data.refreshToken").exists())
            .andDo(print());

        // ------------------ STEP 2 : 레디스 이슈로 서킷브레이커 OPEN ------------------
        circuitBreaker.transitionToOpenState();

        // ------------------ STEP 3 : refresh token 으로 access token 재발급 ------------------
        // given
        JSONObject tokens = new JSONObject(
            actions.andReturn().getResponse().getContentAsString()).getJSONObject("data");
        ReIssueTokenRequest reIssueTokenRequest = ReIssueTokenRequest.builder()
            .refreshToken(tokens.getString("refreshToken"))
            .build();

        // when
        actions = mockMvc.perform(
            post("/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", userAgent)
                .content(objectMapper.writeValueAsString(reIssueTokenRequest)));

        // then
        actions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.httpStatus").value(200))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data.accessToken").exists())
            .andExpect(jsonPath("$.data.refreshToken").exists())
            .andDo(print());
        JSONObject newTokens = new JSONObject(
            actions.andReturn().getResponse().getContentAsString()).getJSONObject("data");
        assertThat(newTokens.getString("accessToken"))
            .isNotEqualTo(tokens.getString("accessToken"));
        assertThat(newTokens.getString("refreshToken"))
            .isNotEqualTo(tokens.getString("refreshToken"));

        // ------------------ STEP 4 : refresh token 으로 access token 재발급 2------------------
        // given
        reIssueTokenRequest = ReIssueTokenRequest.builder()
            .refreshToken(newTokens.getString("refreshToken"))
            .build();

        // when
        actions = mockMvc.perform(
            post("/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", userAgent)
                .content(objectMapper.writeValueAsString(reIssueTokenRequest)));

        // then
        actions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.httpStatus").value(200))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data.accessToken").exists())
            .andExpect(jsonPath("$.data.refreshToken").exists())
            .andDo(print());
        newTokens = new JSONObject(
            actions.andReturn().getResponse().getContentAsString()).getJSONObject("data");
        assertThat(newTokens.getString("accessToken"))
            .isNotEqualTo(tokens.getString("accessToken"));
        assertThat(newTokens.getString("refreshToken"))
            .isNotEqualTo(tokens.getString("refreshToken"));

        // ------------------ STEP 5 : redis 정상화 및 서킷브레이커 CLOSED ------------------
        circuitBreaker.transitionToClosedState();

        // ------------------ STEP 6 : refresh token 으로 access token 재발급 2------------------
        // given
        reIssueTokenRequest = ReIssueTokenRequest.builder()
            .refreshToken(newTokens.getString("refreshToken"))
            .build();

        // when
        actions = mockMvc.perform(
            post("/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", userAgent)
                .content(objectMapper.writeValueAsString(reIssueTokenRequest)));

        // then
        actions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.httpStatus").value(200))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data.accessToken").exists())
            .andExpect(jsonPath("$.data.refreshToken").exists())
            .andDo(print());
        newTokens = new JSONObject(
            actions.andReturn().getResponse().getContentAsString()).getJSONObject("data");
        assertThat(newTokens.getString("accessToken"))
            .isNotEqualTo(tokens.getString("accessToken"));
        assertThat(newTokens.getString("refreshToken"))
            .isNotEqualTo(tokens.getString("refreshToken"));
    }

}
