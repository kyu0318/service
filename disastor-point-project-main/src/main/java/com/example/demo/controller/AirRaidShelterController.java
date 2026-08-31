package com.example.demo.controller;

import com.example.demo.domain.AirRaidShelter;
import com.example.demo.service.AirRaidShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/shelters/air")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AirRaidShelterController {

    private final AirRaidShelterService airRaidShelterService;

    /**
     * 💡 [전체 조회 및 바운딩 박스(지도 영역) 동시 지원]
     * 호출 예시:
     * - /api/shelters/air
     * - /api/shelters/air?minLat=37.4&maxLat=37.6&minLng=126.9&maxLng=127.1
     */
    @GetMapping
    public ResponseEntity<List<AirRaidShelter>> getAirRaidShelters(
            @RequestParam(name = "minLat", required = false) BigDecimal minLat,
            @RequestParam(name = "maxLat", required = false) BigDecimal maxLat,
            @RequestParam(name = "minLng", required = false) BigDecimal minLng,
            @RequestParam(name = "maxLng", required = false) BigDecimal maxLng) {

        List<AirRaidShelter> shelters;

        if (minLat != null && maxLat != null && minLng != null && maxLng != null) {
            shelters = airRaidShelterService.findSheltersByBounds(minLat, maxLat, minLng, maxLng);
        } else {
            shelters = airRaidShelterService.findAllAirRaidShelters();
        }

        return ResponseEntity.ok(shelters);
    }

    /**
     * 💡 [수정] 단일 공습 대피소 상세 조회 (String PK 일치)
     * 호출 예시: /api/shelters/air/AIR_0_0
     */
    @GetMapping("/{id}")
    public ResponseEntity<AirRaidShelter> getShelterById(@PathVariable("id") String id) {
        AirRaidShelter shelter = airRaidShelterService.findShelterById(id);
        return ResponseEntity.ok(shelter);
    }
}