package com.example.demo.repository;

import com.example.demo.domain.FloodShelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
// 💡 엔티티 PK(shlt_id) 타입인 String으로 일치시킵니다.
public interface FloodShelterRepository extends JpaRepository<FloodShelter, String> {

    /**
     * [수해 대피소 바운딩 박스 범위 검색 쿼리 메서드]
     * 위도(lat)와 경도(lot) 범위를 기준으로 현재 지도 화면 영역 내 대피소만 조회합니다.
     */
    List<FloodShelter> findByLatBetweenAndLotBetween(
            BigDecimal minLat, BigDecimal maxLat,
            BigDecimal minLot, BigDecimal maxLot
    );
}