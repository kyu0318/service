package com.example.demo.service;

import com.example.demo.domain.FloodShelter;
import com.example.demo.repository.FloodShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FloodShelterService {

    private final FloodShelterRepository floodShelterRepository;

    /**
     * 수해 대피소 전체 목록 조회
     */
    public List<FloodShelter> findAllFloodShelters() {
        return floodShelterRepository.findAll();
    }

    /**
     * 💡 [수정] 단건 수해 대피소 상세 조회 (PK 타입 String으로 일치)
     */
    public FloodShelter findShelterById(String id) {
        return floodShelterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 수해 대피소가 존재하지 않습니다: " + id));
    }

    /**
     * 💡 [추가] 지도 화면 영역(바운딩 박스) 내 수해 대피소 목록 조회
     */
    public List<FloodShelter> findSheltersByBounds(
            BigDecimal minLat, BigDecimal maxLat,
            BigDecimal minLot, BigDecimal maxLot) {
        return floodShelterRepository.findByLatBetweenAndLotBetween(minLat, maxLat, minLot, maxLot);
    }
}