package com.example.demo.controller;

import com.example.demo.service.AirRaidShelterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// 💡 Spring Boot 3.2.5 표준 WebMvcTest 패키지
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// 💡 Spring Boot 3.2.5 표준 가짜 빈(Mock) 어노테이션
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AirRaidShelterController.class)
class AirRaidShelterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 💡 3.2.5 버전에서는 @MockitoBean 대신 @MockBean 사용
    @MockBean
    private AirRaidShelterService airRaidShelterService;

    @Test
    @DisplayName("공습 대피소 전체 목록 API 정상 응답 테스트")
    void getAllAirRaidSheltersTest() throws Exception {
        // GET /api/shelters/air 요청 시 HTTP 200(OK) 응답 검증
        mockMvc.perform(get("/api/shelters/air"))
                .andExpect(status().isOk());
    }
}