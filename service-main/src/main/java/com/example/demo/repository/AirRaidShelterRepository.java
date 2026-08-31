package com.example.demo.repository;

import com.example.demo.domain.AirRaidShelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirRaidShelterRepository extends JpaRepository<AirRaidShelter, Long> {

    /**
     * 💡 [공습 대피소 바운딩 박스 검색 - Native Query 버전]
     * DB 컬럼의 타입(VARCHAR, DECIMAL 등)과 상관없이 숫자로 강제 형변환(CAST)하여 조회합니다.
     */
    @Query(value = "SELECT * FROM airstrike WHERE " +
            "((CAST(lat AS DOUBLE) BETWEEN :minLat AND :maxLat) AND (CAST(lot AS DOUBLE) BETWEEN :minLng AND :maxLng)) " +
            "OR " +
            "((CAST(lot AS DOUBLE) BETWEEN :minLat AND :maxLat) AND (CAST(lat AS DOUBLE) BETWEEN :minLng AND :maxLng))",
            nativeQuery = true)
    List<AirRaidShelter> findSheltersInBounds(
            @Param("minLat") Double minLat,
            @Param("maxLat") Double maxLat,
            @Param("minLng") Double minLng,
            @Param("maxLng") Double maxLng
    );
}