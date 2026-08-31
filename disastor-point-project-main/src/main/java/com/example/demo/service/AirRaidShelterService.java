package com.example.demo.service;

import com.example.demo.domain.AirRaidShelter;
import com.example.demo.repository.AirRaidShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AirRaidShelterService {

    private final AirRaidShelterRepository airRaidShelterRepository;

    /**
     * 공습 대피소 전체 목록 조회
     */
    public List<AirRaidShelter> findAllAirRaidShelters() {
        return airRaidShelterRepository.findAll();
    }

    /**
     * 💡 [수정] 단일 공습 대피소 조회 (PK가 문자열이므로 String id로 일치)
     */
    public AirRaidShelter findShelterById(String id) {
        return airRaidShelterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공습 대피소가 존재하지 않습니다: " + id));
    }

    /**
     * 💡 [추가] 지도 화면 영역(바운딩 박스) 내 공습 대피소 목록 조회
     */
    public List<AirRaidShelter> findSheltersByBounds(
            BigDecimal minLat, BigDecimal maxLat,
            BigDecimal minLot, BigDecimal maxLot) {
        return airRaidShelterRepository.findByLatBetweenAndLotBetween(minLat, maxLat, minLot, maxLot);
    }
}