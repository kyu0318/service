package com.example.demo.repository;

import com.example.demo.domain.FloodShelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// 1. Spring Data JPA의 Repository 인터페이스 명시
@Repository
// 2. JpaRepository<FloodShelter, Long> 상속
public interface FloodShelterRepository extends JpaRepository<FloodShelter, Long> {

    /**
     * 💡 [수해 시설 바운딩 박스 범위 검색 최종본]
     * 엔티티의 실제 필드(f.lat, f.lot)를 기준으로 화면 사각 영역 내 수해 시설만 조회합니다.
     */
    @Query("SELECT f FROM FloodShelter f WHERE f.lat BETWEEN :minLat AND :maxLat AND f.lot BETWEEN :minLng AND :maxLng")
    List<FloodShelter> findSheltersInBounds(
            @Param("minLat") Double minLat,
            @Param("maxLat") Double maxLat,
            @Param("minLng") Double minLng,
            @Param("maxLng") Double maxLng
    );
}