package com.example.demo.service;

import com.example.demo.domain.EarthquakeShelter;
import com.example.demo.repository.EarthquakeShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 💡 조회 전용 트랜잭션 최적화 완료
public class EarthquakeShelterService {

    private final EarthquakeShelterRepository earthquakeShelterRepository;

    /**
     * 지진 대피소 전체 목록 조회
     */
    public List<EarthquakeShelter> findAllEarthquakeShelters() {
        return earthquakeShelterRepository.findAll();
    }

    /**
     * 단건 지진 대피소 상세 조회 (String PK)
     */
    public EarthquakeShelter findShelterById(String id) {
        return earthquakeShelterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 지진 대피소가 존재하지 않습니다. id=" + id));
    }

    /**
     * 지도 화면 영역(바운딩 박스) 내 지진 대피소 목록 조회
     */
    public List<EarthquakeShelter> findSheltersByBounds(
            BigDecimal minLat, BigDecimal maxLat,
            BigDecimal minLot, BigDecimal maxLot) {
        return earthquakeShelterRepository.findByLatBetweenAndLotBetween(minLat, maxLat, minLot, maxLot);
    }
}