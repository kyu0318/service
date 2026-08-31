package com.example.demo.repository;

import com.example.demo.domain.AirRaidShelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
// 💡 Long을 String으로 변경하여 Service의 findById(String id) 및 Entity의 PK(String)와 일치시킵니다.
public interface AirRaidShelterRepository extends JpaRepository<AirRaidShelter, String> {

    /**
     * [공습 대피소 바운딩 박스 범위 검색 쿼리 메서드]
     * AirRaidShelter 엔티티의 lat(위도)과 lot(경도) 필드를 기준으로 사각 영역 조회를 수행합니다.
     */
    List<AirRaidShelter> findByLatBetweenAndLotBetween(
            BigDecimal minLat, BigDecimal maxLat,
            BigDecimal minLot, BigDecimal maxLot
    );
}