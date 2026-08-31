package com.example.demo.controller;

import com.example.demo.domain.FloodShelter;
import com.example.demo.service.FloodShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/shelters/flood")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FloodShelterController {

    private final FloodShelterService floodShelterService;

    /**
     * 💡 [전체 조회 및 지도 영역(바운딩 박스) 동시 지원]
     * - 좌표 파라미터가 들어오면: 화면 영역 내 수해 대피소만 필터링 조회
     * - 좌표 파라미터가 없으면: 전체 수해 대피소 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<FloodShelter>> getFloodShelters(
            @RequestParam(name = "minLat", required = false) BigDecimal minLat,
            @RequestParam(name = "maxLat", required = false) BigDecimal maxLat,
            @RequestParam(name = "minLng", required = false) BigDecimal minLng,
            @RequestParam(name = "maxLng", required = false) BigDecimal maxLng) {

        List<FloodShelter> shelters;

        if (minLat != null && maxLat != null && minLng != null && maxLng != null) {
            shelters = floodShelterService.findSheltersByBounds(minLat, maxLat, minLng, maxLng);
        } else {
            shelters = floodShelterService.findAllFloodShelters();
        }

        return ResponseEntity.ok(shelters);
    }

    /**
     * 특정 수해 대피소 단건 상세 조회 (PK 타입에 맞춰 String/Long 적용)
     */
    @GetMapping("/{id}")
    public ResponseEntity<FloodShelter> getShelterById(@PathVariable("id") String id) {
        FloodShelter shelter = floodShelterService.findShelterById(id);
        return ResponseEntity.ok(shelter);
    }
}