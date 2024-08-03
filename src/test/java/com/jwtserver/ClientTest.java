package com.jwtserver;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jwtserver.user.adapter.in.make_token.MakeTokenRequest;
import com.jwtserver.user.adapter.in.reissue_token.ReIssueTokenRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class ClientTest extends IntegrationTestSupport {


    @Nested
    @DisplayName("[client test] 권한이 필요한 리소스 접근시 인증토큰이 만료된 경우 클라이언트에서 취하는 요청 시나리오 테스트 1")
    class refreshTokenTest {

        @Test
        @DisplayName("[success] 유효한 Refresh Token 인 경우 인증토큰을 재발급 받고 리소스 접근에 성공한다. 클라이언트는 재 로그인이 필요 없다.")
        void success() throws Exception {
            String userAgent = "test1";

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

            // ------------------ STEP 2 : 권한이 필요한 리소스 접근 ------------------
            // given
            Thread.sleep(2100);
            JSONObject tokens = new JSONObject(
                actions.andReturn().getResponse().getContentAsString()).getJSONObject("data");

            actions = mockMvc.perform(
                post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("User-Agent", userAgent)
                    .header("Authorization", tokens.get("accessToken"))
                    .content(objectMapper.writeValueAsString(request)));

            actions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/errors/invalid-token"))
                .andDo(print());

            // ------------------ STEP 3 : refresh token 으로 access token 재발급 ------------------
            // given
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

            // ------------------ STEP 4 : 재발급 받은 토큰으로 리소스 재접근 ------------------
            actions = mockMvc.perform(
                post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("User-Agent", userAgent)
                    .header("Authorization", newTokens.get("accessToken"))
                    .content(objectMapper.writeValueAsString(request)));

            actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.username").value("user"))
                .andExpect(jsonPath("$.data.hobby").value("study"))
                .andDo(print());
        }
    }


    @Test
    @DisplayName("[error] 만료시간이 종료된 Refresh Token 을 입력한 경우 401 코드와 예외를 발생시킨다. 클라이언트는 재 로그인이 필요하다.")
    void error() throws Exception {
        String userAgent = "test";
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

        // ------------------ STEP 2 : 권한이 필요한 리소스 접근 ------------------
        // given
        Thread.sleep(5100);
        JSONObject tokens = new JSONObject(
            actions.andReturn().getResponse().getContentAsString()).getJSONObject("data");

        actions = mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", userAgent)
                .header("Authorization", tokens.get("accessToken"))
                .content(objectMapper.writeValueAsString(request)));

        actions
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/errors/invalid-token"))
            .andDo(print());

        // ------------------ STEP 3 : refresh token 으로 access token 재발급 ------------------
        // given
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
            .andExpect(status().is4xxClientError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.httpStatus").value(401))
            .andExpect(jsonPath("$.message").value("UNAUTHORIZED"))
            .andExpect(jsonPath("$.data.errorCode").value(3002))
            .andExpect(jsonPath("$.data.errorMessage").value("유효한 리프레시 토큰이 아닙니다"))
            .andDo(print());
    }


    @Test
    @DisplayName("[error] Refresh Token 을 탈취한 사용자가 인증토큰을 재발급 받으려고 하는 경우 (User-Agent 가 다른 경우) 401 코드와 예외를 발생시킨다.")
    void error2() throws Exception {
        String userAgent = "test";
        String anotherUserAgent = "test2";

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

        // ------------------ STEP 2 : 권한이 필요한 리소스 접근 ------------------
        // given
        Thread.sleep(2100);
        JSONObject tokens = new JSONObject(
            actions.andReturn().getResponse().getContentAsString()).getJSONObject("data");

        actions = mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", userAgent)
                .header("Authorization", tokens.get("accessToken"))
                .content(objectMapper.writeValueAsString(request)));

        actions
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/errors/invalid-token"))
            .andDo(print());

        // ------------------ STEP 3 : refresh token 으로 access token 재발급 ------------------
        // given
        ReIssueTokenRequest reIssueTokenRequest = ReIssueTokenRequest.builder()
            .refreshToken(tokens.getString("refreshToken"))
            .build();

        // when
        actions = mockMvc.perform(
            post("/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", anotherUserAgent)
                .content(objectMapper.writeValueAsString(reIssueTokenRequest)));

        // then
        actions
            .andExpect(status().is4xxClientError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.httpStatus").value(401))
            .andExpect(jsonPath("$.message").value("UNAUTHORIZED"))
            .andExpect(jsonPath("$.data.errorCode").value(3002))
            .andExpect(jsonPath("$.data.errorMessage").value("유효한 리프레시 토큰이 아닙니다"))
            .andDo(print());
    }

    @Test
    @DisplayName("[error] Refresh Token 을 탈취한 사용자가 인증토큰을 재발급 받으려고 하는 경우 (최신화된 토큰을 입력하지 않은 경우) 401 코드와 예외를 발생시킨다.")
    void error3() throws Exception {
        String userAgent = "test";

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

        // ------------------ STEP 2 : 권한이 필요한 리소스 접근 ------------------
        // given
        Thread.sleep(2100);
        JSONObject tokens = new JSONObject(
            actions.andReturn().getResponse().getContentAsString()).getJSONObject("data");

        actions = mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-Agent", userAgent)
                .header("Authorization", tokens.get("accessToken"))
                .content(objectMapper.writeValueAsString(request)));

        actions
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/errors/invalid-token"))
            .andDo(print());

        // ------------------ STEP 3 : refresh token 으로 access token 재발급 ------------------
        // given
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

        // ------------------ STEP 4 : 최신화 refresh token 을 응답하지 않아 예외 발생 ------------------
        // given
        reIssueTokenRequest = ReIssueTokenRequest.builder()
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
            .andExpect(status().is4xxClientError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.httpStatus").value(401))
            .andExpect(jsonPath("$.message").value("UNAUTHORIZED"))
            .andExpect(jsonPath("$.data.errorCode").value(3002))
            .andExpect(jsonPath("$.data.errorMessage").value("유효한 리프레시 토큰이 아닙니다"))
            .andDo(print());
    }

}
