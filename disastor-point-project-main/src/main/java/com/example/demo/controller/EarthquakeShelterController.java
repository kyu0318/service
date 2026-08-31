package com.example.demo.controller;

import com.example.demo.domain.EarthquakeShelter;
import com.example.demo.service.EarthquakeShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/shelters/earthquake")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EarthquakeShelterController {

    private final EarthquakeShelterService earthquakeShelterService;

    /**
     * [전체 조회 및 바운딩 박스(지도 영역) 동시 지원]
     */
    @GetMapping
    public ResponseEntity<List<EarthquakeShelter>> getEarthquakeShelters(
            @RequestParam(name = "minLat", required = false) BigDecimal minLat,
            @RequestParam(name = "maxLat", required = false) BigDecimal maxLat,
            @RequestParam(name = "minLng", required = false) BigDecimal minLng,
            @RequestParam(name = "maxLng", required = false) BigDecimal maxLng) {

        List<EarthquakeShelter> shelters;

        if (minLat != null && maxLat != null && minLng != null && maxLng != null) {
            shelters = earthquakeShelterService.findSheltersByBounds(minLat, maxLat, minLng, maxLng);
        } else {
            shelters = earthquakeShelterService.findAllEarthquakeShelters();
        }

        return ResponseEntity.ok(shelters);
    }

    /**
     * 💡 [수정] 단일 지진 대피소 상세 조회 (문자열 PK에 맞추어 String id로 변경)
     * 호출 예시: GET /api/shelters/earthquake/EQ_0_0
     */
    @GetMapping("/{id}")
    public ResponseEntity<EarthquakeShelter> getShelterById(@PathVariable("id") String id) {
        EarthquakeShelter shelter = earthquakeShelterService.findShelterById(id);
        return ResponseEntity.ok(shelter);
    }
}