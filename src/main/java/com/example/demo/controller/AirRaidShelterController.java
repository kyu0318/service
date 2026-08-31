package com.example.demo.controller;

import com.example.demo.domain.AirRaidShelter;
import com.example.demo.service.AirRaidShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 1. JSON 형식의 HTTP 응답 본문(Body) 반환
@RestController
// 2. 공통 기본 URL 경로 매핑 (민방공 대피소 API)
@RequestMapping("/api/shelters/air")
// 3. final 필드 생성자 자동 주입 (Lombok)
@RequiredArgsConstructor
// 4. 프론트엔드 연동 CORS 정책 허용
@CrossOrigin(origins = "*")
public class AirRaidShelterController {

    // 비즈니스 로직 Service 객체 주입
    private final AirRaidShelterService airRaidShelterService;

    /**
     * 💡 [민방공 대피소 목록 조회 API - 전체 조회 및 화면 바운딩 박스 검색 겸용]
     *
     * 1) 화면 이동/줌/검색 시 (동네별 조회):
     *    GET /api/shelters/air?minLat=37.4&maxLat=37.5&minLng=126.9&maxLng=127.0
     * 2) 파라미터 없을 시 (전국 전체 조회):
     *    GET /api/shelters/air
     */
    @GetMapping
    public ResponseEntity<List<AirRaidShelter>> getAirRaidShelters(
            // 화면 사각 영역의 최소 위도 (필수값 아님)
            @RequestParam(value = "minLat", required = false) Double minLat,
            // 화면 사각 영역의 최대 위도 (필수값 아님)
            @RequestParam(value = "maxLat", required = false) Double maxLat,
            // 화면 사각 영역의 최소 경도 (필수값 아님)
            @RequestParam(value = "minLng", required = false) Double minLng,
            // 화면 사각 영역의 최대 경도 (필수값 아님)
            @RequestParam(value = "maxLng", required = false) Double maxLng
    ) {
        // 💡 1. 프론트엔드에서 지도 뷰포트 사각 영역 좌표 4개가 모두 넘어왔을 때
        if (minLat != null && maxLat != null && minLng != null && maxLng != null) {
            List<AirRaidShelter> areaShelters = airRaidShelterService.findSheltersInBounds(minLat, maxLat, minLng, maxLng);
            return ResponseEntity.ok(areaShelters);
        }

        // 💡 2. 좌표 파라미터가 없는 기본 호출일 때 (전체 목록)
        List<AirRaidShelter> allShelters = airRaidShelterService.findAllAirRaidShelters();
        return ResponseEntity.ok(allShelters);
    }

    /**
     * 특정 민방공 대피소 1개 단건 상세 조회
     * 호출 URL 예시: http://localhost:8080/api/shelters/air/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<AirRaidShelter> getShelterById(@PathVariable("id") Long id) {
        // ID 기반으로 특정 대피소 데이터 조회
        AirRaidShelter shelter = airRaidShelterService.findShelterById(id);
        // 조회된 단건 데이터를 HTTP 200 OK 상태와 함께 반환
        return ResponseEntity.ok(shelter);
    }
}