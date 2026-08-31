package com.example.demo.controller;

import com.example.demo.domain.FloodShelter;
import com.example.demo.service.FloodShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // REST API 요청 처리 및 JSON 반환 컨트롤러 선언
@RequestMapping("/api/shelters/flood") // 기본 URL 주소 매핑 (수해 시설)
@RequiredArgsConstructor // final 필드 생성자 자동 완성 (Lombok)
@CrossOrigin(origins = "*") // 프론트엔드 CORS 차단 방지
public class FloodShelterController {

    private final FloodShelterService floodShelterService; // 비즈니스 로직 서비스 주입

    /**
     * 💡 [수해 시설 목록 조회 API - 전체 조회 및 지도 영역 검색 겸용]
     *
     * 1) 화면 이동/줌/검색 시 (영역별 조회):
     *    GET /api/shelters/flood?minLat=37.4&maxLat=37.5&minLng=126.9&maxLng=127.0
     * 2) 파라미터 없을 시 (전체 조회):
     *    GET /api/shelters/flood
     */
    @GetMapping
    public ResponseEntity<List<FloodShelter>> getFloodShelters(
            @RequestParam(value = "minLat", required = false) Double minLat,
            @RequestParam(value = "maxLat", required = false) Double maxLat,
            @RequestParam(value = "minLng", required = false) Double minLng,
            @RequestParam(value = "maxLng", required = false) Double maxLng
    ) {
        // 💡 1. 4개 사각 영역 좌표가 모두 넘어온 경우 -> 현재 지도 화면 내 수해 시설만 조회
        if (minLat != null && maxLat != null && minLng != null && maxLng != null) {
            List<FloodShelter> areaShelters = floodShelterService.findSheltersInBounds(minLat, maxLat, minLng, maxLng);
            return ResponseEntity.ok(areaShelters);
        }

        // 💡 2. 파라미터가 없는 기본 요청인 경우 -> 전체 목록 조회
        List<FloodShelter> allShelters = floodShelterService.findAllFloodShelters();
        return ResponseEntity.ok(allShelters);
    }

    /**
     * 특정 ID의 수해 시설 1건 상세 정보 반환
     * 호출 URL: GET /api/shelters/flood/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<FloodShelter> getShelterById(@PathVariable("id") Long id) {
        FloodShelter shelter = floodShelterService.findShelterById(id);
        return ResponseEntity.ok(shelter);
    }
}